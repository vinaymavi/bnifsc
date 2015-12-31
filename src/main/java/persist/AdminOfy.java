package persist;

import entities.Admin;
import com.googlecode.objectify.Key;

import java.util.List;

import static persist.OfyService.ofy;

/**
 * Created by vinaymavi on 28/06/15.
 * This Objectify class adding and getting admin to datastore.
 */
public class AdminOfy {
    /**
     * Add Admin to datastore.
     *
     * @param admin
     * @return Key<Admin>
     */
    public Key<Admin> save(Admin admin) {
        return ofy().save().entity(admin).now();
    }

    /**
     * Load Admin by key.
     *
     * @param key
     * @return Admin.
     */
    public Admin loadByKey(Key<Admin> key) {
        return ofy().load().key(key).now();
    }

    /**
     * Load Admin by Email address.
     *
     * @param email
     * @return List<Admin>
     */
    public List<Admin> loadByEmail(String email) {
        return ofy().load().type(Admin.class).filter("email", email).list();
    }

}
