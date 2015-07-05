package persist;

import bnifsc.entities.Feedback;
import com.googlecode.objectify.Key;

import static persist.OfyService.ofy;

/**
 * Objectify class for adding and getting data in datastore.
 */
public class FeedbackOfy {
    /**
     * Save feedback in datastore.
     * @param feedback
     * @return {Key<Feedback>}
     */
    public Key<Feedback> save(Feedback feedback) {
        return ofy().save().entity(feedback).now();
    }

    /**
     * Load Feedback by Key.
     * @param key
     * @return @code{Feedback}.
     */
    public Feedback loadByKey(Key<Feedback> key){
        return ofy().load().key(key).now();
    }

}
