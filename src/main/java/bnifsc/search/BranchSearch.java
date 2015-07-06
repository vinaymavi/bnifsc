package bnifsc.search;

import bnifsc.entities.Branch;
import bnifsc.util.BranchIndexer;
import com.google.appengine.api.search.*;
import persist.BranchOfy;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by vinaymavi on 05/07/15.
 * This class is used to get search result from Branch Search Index.
 */
public class BranchSearch {
    public static final int RESULT_LIMIT = 20;
    public static final Logger logger = Logger.getLogger(BranchSearch.class.getName());

    /**
     * Search result from Branch search Index.
     *
     * @param bankName
     * @param query
     */
    public static Map search(String bankName, String query, String lastCursor) {
        Cursor cursor;

        /**
         * Query Generation.
         */
        String queryString = "";
        if (bankName != null) {
            queryString = "bankName:(" + bankName + ") AND ";
        }
        queryString += "(" + query + ")";

        // Build the QueryOptions
        QueryOptions options;
        if (lastCursor == null) {
            logger.warning("crate new cursor");
            options = QueryOptions.newBuilder()
                    .setCursor(Cursor.newBuilder().build())
                    .build();
        } else {
            options = QueryOptions.newBuilder()
                    .setCursor(Cursor.newBuilder().build(lastCursor))
                    .build();
        }

        //  Build the Query and run the search
        Query searchQuery = Query.newBuilder().setOptions(options).build(queryString);
//        TODO can call index from BranchIndexer
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(BranchIndexer.INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        Results<ScoredDocument> result = index.search(searchQuery);
        cursor = result.getCursor();
        logger.warning("Result found in query #" + queryString + "=" + result.getNumberFound());
        logger.warning("Result returned in query #" + queryString + "=" + result.getNumberReturned());

        if (result != null && result.getNumberFound() > 0) {
            Map resultMap = new HashMap();
            Collection<ScoredDocument> listOfDocs = result.getResults();
            List<Branch> branches = new ArrayList<Branch>();
            for (ScoredDocument doc : listOfDocs) {
                branches.add(BranchOfy.loadById(doc.getId()));
            }
            resultMap.put("branches", branches);
            logger.warning("cursor" + cursor);
            if (cursor != null) {
                resultMap.put("cursor", cursor.toWebSafeString());
            } else {
                logger.warning("next cursor is null.");
                resultMap.put("cursor", cursor);
            }
            return resultMap;
        }
        logger.warning("No result found");
        return Collections.EMPTY_MAP;


    }
}
