package bnifsc.entites;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import java.util.Calendar;

/**
 * Created by vinaymavi on 09/05/15.
 */
public class Feedback {
    private static final String ENTITY_NAME="Feedback";
    private static final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    private String feedback;

    public Feedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Feedback save(){
        Entity entity = new Entity(ENTITY_NAME);
        entity.setProperty("feedback",this.getFeedback());
        entity.setProperty("timestamp", Calendar.getInstance().getTimeInMillis());
        datastoreService.put(entity);
        return this;
    }
}
