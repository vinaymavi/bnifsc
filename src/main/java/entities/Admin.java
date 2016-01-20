package entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

import java.util.Date;

/**
 * Created by vinaymavi on 16/05/15.
 * <p/>
 * This is Objectify Admin Entity.
 */
@Entity(name = "Admin")
public class Admin {
    @Id
    private Long id;
    @Index
    private String email;
    private String name;
    private Date date;

    public Admin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @OnSave
    void addDefaultDate() {
        this.date = new Date();
    }
}
