package entities;

import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import persist.BankOfy;
import util.Word;
import com.google.appengine.api.datastore.Email;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import org.apache.commons.lang3.text.WordUtils;

import java.net.URL;
import java.util.Date;

/**
 * Created by vinaymavi on 27/08/15.
 */
@Entity(name = "Bank")
public class Bank {
    @Id
    String id;
    @Index
    String name;
    @Index
    String acronym;
    URL image;
    /*Bank head office details.*/
    @Index
    String state;
    @Index
    String district;
    @Index
    String city;
    @Index
    String address;
    Email email;
    String phone;
    String mobile;
    @Index
    String pinCode;
    @Index
    Boolean popular;
    Date updateDate;
    Date addDate;

    public Bank() {
    }

    public Bank(String name) {
        this.name = WordUtils.capitalizeFully(name).trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name).trim();
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = Word.capitalize(state).trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = Word.capitalize(district).trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = Word.capitalize(city).trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = Word.capitalize(address).trim();
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile.trim();
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    /**
     * @param line String
     * @return Bank
     */
    public static Bank createFromCSV(String line) {
        Bank bank = BankOfy.loadByName(WordUtils.capitalizeFully(line).trim());
        if (bank != null) {
            bank.setUpdateDate(new Date());
        } else {
            bank = new Bank(line);
        }
        return bank;
    }

    public Key createKey() {
        Key key = KeyFactory.createKey("Bank", this.getId());
        return key;
    }
}
