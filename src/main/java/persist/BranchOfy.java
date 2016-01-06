package persist;

import entities.Bank;
import entities.Branch;
import com.googlecode.objectify.Key;

import java.util.List;
import java.util.logging.Logger;

import static persist.OfyService.ofy;

/**
 * Created by vinaymavi on 28/06/15.
 * This is Objectify class to get and set values in datastore.
 */
public class BranchOfy {
    public static Logger logger = Logger.getLogger(BranchOfy.class.getName());

    /**
     * Save Branch to Datastore.
     *
     * @param branch
     * @return key<Branch>
     */
    public static Key<Branch> save(Branch branch) {
        logger.warning("Add branch =" + branch.getName());
        Key key = ofy().save().entity(branch).now();
        logger.warning("Branch added=" + branch.getName());
        return key;

    }

    /**
     * Load Branch by Key.
     *
     * @param key
     * @return Branch
     */
    public static Branch loadByKey(Key<Branch> key) {
        logger.warning("Load by key=" + key);
        Branch branch = ofy().load().key(key).now();
        logger.warning("Branch=" + branch);
        return branch;
    }

    /**
     * Load Branches by ifsc code.
     *
     * @param ifsc
     * @return List<Branch>
     */
    public static List<Branch> loadByIFSC(String ifsc) {
        /*TODO return single Branch instead of list.*/
        logger.warning("ifsc=" + ifsc);
        List<Branch> branchList = ofy().load().type(Branch.class).filter("ifsc", ifsc).list();
        logger.warning("branch list size=" + branchList.size());
        return branchList;
    }

    /**
     * Load List of bank names.
     *
     * @return List<String>
     */
    @Deprecated
    /*Use BankOfy to retrieve banks list.*/
    public static List<Branch> banksList() {
        /*TODO implement Bank list function inside BankOfy.*/
        logger.warning("Load bank list");
        List<Branch> branchList = ofy().load().type(Branch.class).project("bank.name").distinct(true).list();
        logger.warning("Branch list size=" + branchList.size());
        return branchList;
    }

    /**
     * Load list of states by bank name.
     *
     * @param bankName
     * @return List<String>
     */
    public static List<Branch> statesList(String bankName) {
        logger.warning("State by bank name=" + bankName);
        Bank bank = BankOfy.loadByName(bankName);
        List<Branch> branchList = ofy().load().type(Branch.class).filter("bank", bank).project("state")
                .distinct(true).list();
        logger.warning("branch list size=" + branchList.size());
        return branchList;
    }

    /**
     * Load list of districts by bank and state name.
     *
     * @param bankName
     * @param stateName
     * @return List<String>
     */
    public static List<Branch> districtsList(String bankName, String stateName) {
        logger.warning("District list by bank=" + bankName + ",stateName=" + stateName);
        Bank bank = BankOfy.loadByName(bankName);
        List<Branch> branchList = ofy().load().type(Branch.class).filter("bank", bank).filter("state",
                stateName).project("district").distinct(true).list();
        logger.warning("branch list size=" + branchList.size());
        return branchList;
    }

    /**
     * List of branches by bank,state and district name.
     *
     * @param bankName
     * @param stateName
     * @param districtName
     * @return List<Branch>
     */
    public static List<Branch> branches(String bankName, String stateName, String districtName) {
        logger.warning("Branch list by bank=" + bankName + ",state=" + stateName + ",districtName=" + districtName);
        Bank bank = BankOfy.loadByName(bankName);
        List<Branch> branchList = ofy().load().type(Branch.class).filter("bank", bank).filter("state",
                stateName).filter("district", districtName).list();
        logger.warning("branch list size=" + branchList.size());
        return branchList;
    }

    /**
     * Return Branch by id.
     *
     * @param id
     * @return Branch.
     */
    public static Branch loadById(String id) {
        logger.warning("id=" + id);
        Branch branch = ofy().load().type(Branch.class).id(id).safe();
        logger.warning("branch name=" + branch.getName());
        return branch;
    }
}
