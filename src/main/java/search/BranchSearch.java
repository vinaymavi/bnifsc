package search;

import entities.Branch;
import util.BranchIndexer;
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
        Cursor cursor = null;

        /**
         * Query Generation.
         */
        String searchString = new BranchSearch().createQuery(bankName, query);


        // Build the QueryOptions
        QueryOptions options;
        if (lastCursor == null) {
            logger.warning("crate new cursor");
            options = QueryOptions.newBuilder()
                    .setCursor(Cursor.newBuilder().build())
                    .setLimit(20)
                    .build();
        } else {
            options = QueryOptions.newBuilder()
                    .setCursor(Cursor.newBuilder().build(lastCursor))
                    .setLimit(20)
                    .build();
        }

        //  Build the Query and run the search
        Query searchQuery = Query.newBuilder().setOptions(options).build(searchString);
//        TODO call index from BranchIndexer
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(BranchIndexer.INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        Results<ScoredDocument> result = index.search(searchQuery);
        logger.warning("Result found in query #" + searchString + "=" + result.getNumberFound());
        logger.warning("Result returned in query #" + searchString + "=" + result.getNumberReturned());

        if (result != null && result.getNumberFound() > 0) {
            Map resultMap = new HashMap();
            Collection<ScoredDocument> listOfDocs = result.getResults();
            List<Branch> branches = new ArrayList<Branch>();
            for (ScoredDocument doc : listOfDocs) {
                branches.add(BranchOfy.loadById(doc.getId()));
                cursor = result.getCursor();
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

    /**
     * Create query
     *
     * @param bankName
     * @param query
     * @return {String} Google Cloud query
     */
    private String createQuery(String bankName, String query) {
        String searchString = "";
        if (bankName != null) {
            searchString = "bankName:(" + bankName + ") AND ";
        }

//        queryString += "(" + query.split(" ") + ")";
        String queryString = "";
        StringTokenizer tokens = new StringTokenizer(query, " ");
        if (tokens.hasMoreTokens()) {
            queryString += tokens.nextToken();
        }

        while (tokens.hasMoreTokens()) {
            queryString += " OR " + tokens.nextToken();
        }
        searchString += "( " + queryString + " )";
        return searchString;
    }
}
