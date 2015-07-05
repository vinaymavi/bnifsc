package persist;

import bnifsc.entities.Branch;
import com.googlecode.objectify.Key;

import java.util.List;

import static persist.OfyService.ofy;

/**
 * Created by vinaymavi on 28/06/15.
 * This is Objectify class to get and set values in datastore.
 */
public class BranchOfy {
    /**
     * Save Branch to Datastore.
     *
     * @param branch
     * @return key<Branch>
     */
    public Key<Branch> save(Branch branch) {
        return ofy().save().entity(branch).now();
    }

    /**
     * Load Branch by Key.
     *
     * @param key
     * @return Branch
     */
    public Branch loadByKey(Key<Branch> key) {
        return ofy().load().key(key).now();
    }

    /**
     * Load Branches by ifsc code.
     *
     * @param ifsc
     * @return List<Branch>
     */
    public List<Branch> loadByIFSC(String ifsc) {
        return ofy().load().type(Branch.class).filter("ifsc", ifsc).list();
    }

    /**
     * Load List of bank names.
     *
     * @return List<String>
     */
    public List<Branch> banksList() {
        return ofy().load().type(Branch.class).project("bankName").distinct(true).list();
    }

    /**
     * Load list of states by bank name.
     *
     * @param bankName
     * @return List<String>
     */
    public List<Branch> statesList(String bankName) {
        return ofy().load().type(Branch.class).filter("bankName", bankName).project("state").distinct(true).list();
    }

    /**
     * Load list of districts by bank and state name.
     *
     * @param bankName
     * @param stateName
     * @return List<String>
     */
    public List<Branch> districtsList(String bankName, String stateName) {
        return ofy().load().type(Branch.class).filter("bankName", bankName).filter("state", stateName).project("district").distinct(true).list();
    }

    /**
     * List of branches by bank,state and district name.
     *
     * @param bankName
     * @param stateName
     * @param districtName
     * @return List<Branch>
     */
    public List<Branch> branches(String bankName, String stateName, String districtName) {
        return ofy().load().type(Branch.class).filter("bankName", bankName).filter("state", stateName).filter("district", districtName).list();
    }
}
