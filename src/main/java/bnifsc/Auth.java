package bnifsc;

import entities.Admin;
import com.google.appengine.api.users.User;
import persist.AdminOfy;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vinaymavi on 16/05/15.
 *
 * @Description validate logged in user with email address.
 */
public class Auth {
    private final static Logger logger = Logger.getLogger(Auth.class
            .getName());
    private final static String DEFAULT_ADMIN = "vinaymavi@gmail.com";

    /**
     * @param user
     * @return boolean
     * @description validate logged in user.
     */
    public static boolean validate(User user) {
        String email = user.getEmail();
        AdminOfy af = new AdminOfy();
        List<Admin> admins = af.loadByEmail(email);
        if (admins.size() <= 0 && !email.contains(DEFAULT_ADMIN)) {
            logger.warning("Invailid User=" + email);
//            TODO an exception throw required.
            return false;
        }
        logger.warning("valid login=" + email);
        return true;
    }
}
