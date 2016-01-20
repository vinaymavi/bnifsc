package persist;

import entities.Bank;
import com.googlecode.objectify.Key;

import java.util.List;
import java.util.logging.Logger;

import static persist.OfyService.ofy;

/**
 * Created by vinaymavi on 27/08/15.
 */
public class BankOfy {
    private final static Logger logger = Logger.getLogger(BankOfy.class.getName());

    /**
     * @param bank
     * @return Key<Bank>
     */
    public Key<Bank> save(Bank bank) {
        return ofy().save().entity(bank).now();
    }

    public Bank loadByKey(Key<Bank> key) {
        logger.warning("Get bank by key = " + key.toString());
        return ofy().load().key(key).now();
    }


    public static Bank loadByName(String name) {
        logger.warning("Req Get bank by name= " + name);
        Bank bank = ofy().load().type(Bank.class).filter("name", name).first().now();
        logger.warning("Resp Get bank by name= " + name + ",size=" + bank);
        return bank;
    }

    public List<Bank> listAll() {
        logger.warning("Req all banks list");
        List<Bank> bankList = ofy().load().type(Bank.class).list();
        logger.warning("Resp all banks list size = " + bankList.size());
        return bankList;
    }
}
