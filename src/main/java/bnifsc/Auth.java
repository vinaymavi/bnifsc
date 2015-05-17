package bnifsc;

import bnifsc.entites.BnifscUser;
import com.google.appengine.api.users.User;

import java.util.logging.Logger;

/**
 * Created by vinaymavi on 16/05/15.
 *
 * @Description validate logged in user with email address.
 */
public class Auth {
    private final static Logger logger = Logger.getLogger(Auth.class
            .getName());

    /**
     * @param user
     * @return boolean
     * @description validate logged in user.
     */
    public static boolean validate(User user) {
        String email = user.getEmail();
        BnifscUser bnifscUser = BnifscUser.userByEmail(email);
        if (bnifscUser == null) {
            logger.warning("Invailid User=" + email);
//            TODO an exception throw required.
            return false;
        }
        logger.warning("valid login=" + email);
        return true;
    }
}
