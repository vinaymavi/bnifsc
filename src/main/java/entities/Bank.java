package entities;

import com.google.appengine.api.datastore.EmbeddedEntity;
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
    Long id;
    @Index
    String name;
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
        this.name = name;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name);
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
        this.state = Word.capitalize(state);
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = Word.capitalize(district);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = Word.capitalize(city);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = Word.capitalize(address);
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

    @OnSave
    void addDefaultDate() {
        this.addDate = new Date();
    }

    /**
     * @param line String
     * @return Bank
     */
    public static Bank createFromCSV(String line) {
        Bank bank = new Bank(line);
        bank.setAddDate(new Date());
        return bank;
    }

    public EmbeddedEntity createEmbeddedEntity() {
        EmbeddedEntity embeddedEntity = new EmbeddedEntity();
        embeddedEntity.setProperty("name", this.getName());
        embeddedEntity.setProperty("image", this.getImage().toString());
        embeddedEntity.setProperty("state", this.getState());
        embeddedEntity.setProperty("district", this.getDistrict());
        embeddedEntity.setProperty("city", this.getCity());
        embeddedEntity.setProperty("address", this.getAddress());
        embeddedEntity.setProperty("email", this.getEmail());
        embeddedEntity.setProperty("phone", this.getPhone());
        embeddedEntity.setProperty("mobile", this.getMobile());
        embeddedEntity.setProperty("pinCode", this.getPinCode());
        embeddedEntity.setProperty("popular", this.getPopular());
        embeddedEntity.setProperty("updateDate", this.getUpdateDate());
        embeddedEntity.setProperty("addDate", this.getAddDate());
        return embeddedEntity;
    }
}
