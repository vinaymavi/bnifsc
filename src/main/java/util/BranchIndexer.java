package util;

import entities.Branch;
import com.google.appengine.api.search.*;

import java.util.logging.Logger;

/**
 * Created by vinaymavi on 05/07/15.
 * This class is responsible for.
 * 1. Add document to search index.
 */
public class BranchIndexer {
    public static final String INDEX_NAME = "Branch";
    public static final Logger logger = Logger.getLogger(BranchIndexer.class.getName());

    /**
     * Create search index document from @code{{entities.Branch}}
     *
     * @param branch
     * @return {{Document}}
     */
    public Document createDoc(Branch branch) {
        String fullText = branch.getState() + " " +
                branch.getDistrict() + " " +
                branch.getCity() + " " +
                branch.getAddress();
        Document doc = Document.newBuilder()
                .setId(branch.getIfsc())
                .addField(Field.newBuilder().setName("ifsc").setText(branch.getIfsc()))
                .addField(Field.newBuilder().setName("fullText").setText(fullText))
                .addField(Field.newBuilder().setName("bankName").setText(branch.getBank().getName()))
                .build();
        logger.warning("Document created.");
        return doc;
    }

    /**
     * Put @code {{Document}} to index.
     *
     * @param doc
     */
    public static void put(Document doc) {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        try {
            index.put(doc);
            logger.warning("Document pushed.");
        } catch (PutException e) {
            logger.warning(e.getMessage());
            if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
                BranchIndexer.put(doc);
            }
        }
    }
}
