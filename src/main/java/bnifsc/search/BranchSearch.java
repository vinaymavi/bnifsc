package bnifsc.search;

import bnifsc.entities.Branch;
import bnifsc.util.BranchIndexer;
import com.google.appengine.api.search.*;
import persist.BranchOfy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    public static List<Branch> search(String bankName, String query) {
        // TODO implement cursor for more results.
        /**
         * Query Generation.
         */
        String queryString = "";
        if (bankName != null) {
            queryString = "bankName:(" + bankName + ") AND ";
        }
        queryString += "(" + query + ")";

        // Build the QueryOptions
        QueryOptions options = QueryOptions.newBuilder()
                .setLimit(RESULT_LIMIT)
                .build();

        //  Build the Query and run the search
        Query searchQuery = Query.newBuilder().setOptions(options).build(queryString);
//        TODO can call index from BranchIndexer
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(BranchIndexer.INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        Results<ScoredDocument> result = index.search(query);

        logger.warning("Result found in query #" + searchQuery + "=" + result.getNumberFound());
        logger.warning("Result returned in query #" + searchQuery + "=" + result.getNumberReturned());

        if (result != null && result.getNumberFound() > 0) {
            Collection<ScoredDocument> listOfDocs = result.getResults();
            List<Branch> branches = new ArrayList<Branch>();
            for (ScoredDocument doc : listOfDocs) {
                branches.add(BranchOfy.loadById(doc.getId()));
            }
            return branches;
        }
        logger.warning("No result found");
        return Collections.EMPTY_LIST;


    }
}
