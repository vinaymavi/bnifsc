package entities;


import com.google.appengine.api.datastore.Email;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

import java.util.Date;


/**
 * This is simple POJO class of Objectify entity.
 */
@Entity(name = "Feedback")
public class Feedback {
    @Id
    private Long id;
    private String feedback;
    @Index
    private Email email;
    private Date date;

    public Feedback() {

    }

    public Feedback(String feedback, Email email) {
        this.feedback = feedback;
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @OnSave
     void addDefaultDate() {
        this.date = new Date();
    }

}
