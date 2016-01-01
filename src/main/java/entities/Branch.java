package entities;

import persist.BankOfy;
import util.Word;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import persist.BranchOfy;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(name = "Branch")
public class Branch {
    @Id
    private String id;
    @Index
    @Deprecated
    private String bankName;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Word.capitalize(name);
    }

    @Index
    private String state;
    @Index
    private String district;
    @Index
    private String city;
    @Load
    Ref<Bank> bank;
    private String branchCode;
    private String custCare;
    private String email;
    private String mobile;
    private String phone;
    private String address;
    @Index
    private String ifsc;
    @Index
    private String micr;
    @Index
    private String swift;
    @Index
    private String pinCode;
    private Date addDate;
    private Date updateDate;
    private String cursor;

    public Branch() {
    }

    public Bank getBank() {
        return bank.get();
    }

    public void setBank(Bank bank) {
        this.bank = Ref.create(bank);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = Word.capitalize(city);
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = Word.capitalize(bankName);
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

    public String getCustCare() {
        return custCare;
    }

    public void setCustCare(String custCare) {
        this.custCare = custCare.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Word.capitalize(email);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = Word.capitalize(address);
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc.trim();
    }

    public String getMicr() {
        return micr;
    }

    public void setMicr(String micr) {
        this.micr = micr.trim();
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift.trim();
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode.trim();
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

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor.trim();
    }

    /**
     * Create Branch from csvLine.
     *
     * @param branchCsv
     * @return @code{{Branch}}
     */

    public static Branch fromCSVLine(String branchCsv) {
        Branch branch;
        BranchOfy bf = new BranchOfy();
        Logger logger = Logger.getLogger(Branch.class.getName());
        logger.warning("str=" + branchCsv);
        String[] branchPros = branchCsv.split("\";\"");
        logger.warning("Array Length" + branchPros.length);
        for (String str : branchPros) {
            logger.warning(str);
        }
        //Getting props from branch str.
        String bankName = WordUtils.capitalizeFully(StringUtils.substringAfter(branchPros[0], "\""));
        logger.warning(bankName);
        Bank bank = BankOfy.loadByName(bankName);
//        Bank bank = null;
        String state = WordUtils.capitalizeFully(StringUtils.substringBefore(branchPros[8], "\""));
        logger.warning(state);
        String district = WordUtils.capitalizeFully(branchPros[7]).trim();
        String city = WordUtils.capitalizeFully(branchPros[6]).trim();
        String branchName = WordUtils.capitalizeFully(branchPros[3]).trim();
        String custCare;
        String email;
        String mobile;
        String phone = branchPros[4].split("\\.")[0].trim();
        String address = WordUtils.capitalizeFully(branchPros[5]).trim();

        String ifsc = branchPros[1].trim();
        String branchCode = "";
        if (ifsc.length() > 6) {
            branchCode = ifsc.substring(ifsc.length() - 6);
        }
        String micr = branchPros[2].split("\\.")[0].trim();
        String swift;

        // Pin code extraction from address.
        Pattern pinCodePattern = Pattern.compile("([- 0-9]{6,10})");
        Matcher matcher = pinCodePattern.matcher(address);
        String pinCode = "";
        if (matcher.find()) {
            pinCode = matcher.group();
            if (pinCode.contains("-")) {
                pinCode = StringUtils.substringAfter(pinCode, "-");
            }
        }
        logger.warning("PinCode=" + pinCode);
        //Add and update date.
        Date updateDate;
        Date addDate;
        List<Branch> branchList = bf.loadByIFSC(ifsc);
        if (branchList.size() > 0) {
            branch = branchList.get(0);
            updateDate = new Date();
            addDate = branch.getAddDate();
        } else {
            updateDate = null;
            addDate = new Date();
            branch = new Branch();
        }

        //Start Binding to Branch.
        branch.setName(branchName);
        branch.setState(state);
        branch.setDistrict(district);
        branch.setCity(city);
        branch.setPhone(phone);
        branch.setAddress(address);
        branch.setIfsc(ifsc);
        branch.setBranchCode(branchCode);
        branch.setMicr(micr);
        branch.setPinCode(pinCode);
        branch.setAddDate(addDate);
        branch.setUpdateDate(updateDate);
        branch.setBank(bank);

        return branch;
    }
}
