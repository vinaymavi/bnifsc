package bnifsc.entites;

import bnifsc.util.BulkUpload;
import com.google.appengine.api.datastore.*;

import java.util.logging.Logger;

/**
 * Created by vinaymavi on 16/05/15.
 *
 * @Description This class responsible for adding and getting BnifscUser in datastore.
 */
public class BnifscUser {
    public static String ENTITY = "User";
    private String email;
    private String name;
    public static final int DEFAULT_LIMIT = 20;
    private final static Logger logger = Logger.getLogger(BulkUpload.class
            .getName());
    private final static DatastoreService datastore = DatastoreServiceFactory
            .getDatastoreService();

    /**
     * @param email Email address of user.
     * @return BnifscUser
     * @descriptioin get BnifscUser by email address.
     */
    public static BnifscUser userByEmail(String email) {
        Query query = new Query(ENTITY);
        BnifscUser user = null;
        Query.Filter emailFilter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        query.setFilter(emailFilter);
        PreparedQuery pq = datastore.prepare(query);
        for (Entity entity : pq.asIterable()) {
            user = BnifscUser.createUser(entity);
        }
        return user;
    }

    /**
     * @param entity
     * @return BnifscUser
     * @description Create BnifscUser from Entity.
     */
    private static BnifscUser createUser(Entity entity) {
        BnifscUser user = new BnifscUser();
        user.setEmail((String) entity.getProperty("email"));
        user.setName((String) entity.getProperty("name"));
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
