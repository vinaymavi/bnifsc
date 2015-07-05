package bnifsc.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import persist.BranchOfy;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(name = "Branch")
public class Branch {
    @Id
    private String id;
    @Index
    private String bankName;
    @Index
    private String state;
    @Index
    private String district;
    private String city;
    private String branchName;
    private String branchCode;
    private String custCare;
    private String email;
    private String mobile;
    private String phone;
    private String address;
    @Index
    private String ifsc;
    private String micr;
    private String swift;
    private String pinCode;
    private Date addDate;
    private Date updateDate;

    public Branch() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCustCare() {
        return custCare;
    }

    public void setCustCare(String custCare) {
        this.custCare = custCare;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getMicr() {
        return micr;
    }

    public void setMicr(String micr) {
        this.micr = micr;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @OnSave
    void addDefaultDate() {
        this.addDate = new Date();
    }

    /**
     * Create Branch from csvLine.
     *
     * @param branchCsv
     * @return @code{{Branch}}
     */
    public static Branch fromCSVLine(String branchCsv) {
        Branch branch = new Branch();
        BranchOfy bf = new BranchOfy();

        String[] branchPros = branchCsv.split("\",\"");
        //Getting props from branch.
        String bankName = StringUtils.substringAfter(WordUtils.capitalizeFully(branchPros[0]).trim(), "\"");
        String state = StringUtils.substringBefore(WordUtils.capitalizeFully(branchPros[8]).trim(), "\"");
        String district = WordUtils.capitalizeFully(branchPros[7]).trim();
        String city = WordUtils.capitalizeFully(branchPros[6]).trim();
        String branchName = WordUtils.capitalizeFully(branchPros[3]).trim();
        String custCare;
        String email;
        String mobile;
        String phone = branchPros[5].split("\\.")[0].trim();
        String address = WordUtils.capitalizeFully(branchPros[4]).trim();

        String ifsc = branchPros[1].trim();
        String branchCode = ifsc.substring(ifsc.length() - 6);
        String micr = branchPros[2].split("\\.")[0].trim();
        String swift;
        Pattern pinCodePattern = Pattern.compile("([0-9]{6})");
        Matcher matcher = pinCodePattern.matcher(address);
        String pinCode = "";
        if (matcher.find()) {
            pinCode = matcher.group();
        }

        //Add and update date.
        Date updateDate;
        Date addDate;
        List<Branch> branchList = bf.loadByIFSC(ifsc);
        if (branchList.size() > 0) {
            Branch b = branchList.get(0);
            updateDate = new Date();
            addDate = b.getAddDate();
        } else {
            updateDate = null;
            addDate = new Date();
        }

        //Start Bind to Branch.
        branch.setBankName(bankName);
        branch.setState(state);
        branch.setDistrict(district);
        branch.setCity(city);
        branch.setBranchName(branchName);
        branch.setPhone(phone);
        branch.setAddress(address);
        branch.setIfsc(ifsc);
        branch.setBranchCode(branchCode);
        branch.setMicr(micr);
        branch.setPinCode(pinCode);
        branch.setAddDate(addDate);
        branch.setUpdateDate(updateDate);

        return branch;
    }
}
