// Copyright 2012 Google Inc. All Rights Reserved.

package mapreduce;

import entities.Branch;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.MapOnlyMapper;

import java.util.Random;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create Entities and push document to search index.
 */
public class BranchMapper extends MapOnlyMapper<byte[], Entity> {

    private static final long serialVersionUID = 409204195454478863L;
    public static Logger logger = Logger.getLogger(BranchMapper.class.getName());
    private final String kind;
    private final Random random = new Random();

    public BranchMapper(String kind) {
        this.kind = checkNotNull(kind, "Null kind");
    }

    @Override
    public void map(byte[] csvLine) {
        String branchCsv = new String(csvLine);
        logger.warning("********************** Branch Update start **********************");
        logger.warning("branch csv line=" + branchCsv);
        Branch branch = Branch.fromCSVLine(branchCsv);
        //Search index.
        //BranchIndexer bsi = new BranchIndexer();
        //BranchIndexer.put(bsi.createDoc(branch));

        //Entity Creator
        // TODO put entity creation part to Branch class.
        Entity entity = new Entity(kind, branch.getIfsc());
        entity.setProperty("name", branch.getName());
        entity.setProperty("state", branch.getState());
        entity.setProperty("district", branch.getDistrict());
        entity.setProperty("city", branch.getCity());
        entity.setProperty("branchCode", branch.getBranchCode());
        entity.setProperty("phone", branch.getPhone());
        entity.setProperty("address", branch.getAddress());
        entity.setProperty("ifsc", branch.getIfsc());
        entity.setProperty("micr", branch.getMicr());
        entity.setProperty("pinCode", branch.getPinCode());
        entity.setProperty("addDate", branch.getAddDate());
        entity.setProperty("updateDate", branch.getUpdateDate());
        entity.setProperty("bank", branch.getBank().createKey());
        emit(entity);
    }

}
