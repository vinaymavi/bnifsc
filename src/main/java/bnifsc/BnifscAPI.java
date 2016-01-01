package bnifsc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import entities.Admin;
import entities.Bank;
import entities.Branch;
import entities.Feedback;
import org.apache.commons.lang3.text.WordUtils;
import search.BranchSearch;
import util.BulkUpload;
import seo.SiteMap;

import util.Word;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.users.User;
import persist.AdminOfy;
import persist.BankOfy;
import persist.BranchOfy;
import persist.FeedbackOfy;

import javax.annotation.Nullable;
import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(name = "bnifsc",
        version = "v2",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE})

public class BnifscAPI {

    private final static Logger logger = Logger.getLogger(BnifscAPI.class
            .getName());
    private final static BankOfy bankOfy = new BankOfy();
    private final static BranchOfy branchOfy = new BranchOfy();

    /**
     * Add branch to datastore.
     */
//    TODO add branch to search index.
    @ApiMethod(name = "admin.addBranch")
    public Branch addBranch(@Named("bankName") String bankName,
                            @Named("branchName") String name,
                            @Named("ifsc") String ifsc,
                            @Named("micr") String micr, @Named("swift") String swift,
                            @Named("email") String email, @Named("mobile") String mobile,
                            @Named("customerCare") String custCare,
                            @Named("phone") String phone, @Named("state") String state,
                            @Named("district") String district,
                            @Named("city") String city,
                            @Named("branchCode") String branchCode,
                            @Named("address") String address,
                            @Named("pin") String pinCode,
                            User user) throws Exception {
        if (Auth.validate(user)) {
            Branch branch = null;
            List<Branch> branchList = BranchOfy.loadByIFSC(ifsc.trim());
            if (branchList.size() > 0) {
                branch = branchList.get(0);
                branch.setUpdateDate(new Date());
            } else {
                branch = new Branch();
                branch.setAddDate(new Date());
            }
            Bank bank = bankOfy.loadByName(Word.capitalize(bankName));
            if (bank == null) {
                throw new Exception("bank not exists name=" + bankName);
            }

            branch.setBank(bank);
            branch.setBankName(bankName);
            branch.setIfsc(ifsc);
            branch.setMicr(micr);
            branch.setSwift(swift);
            branch.setEmail(email);
            branch.setMobile(mobile);
            branch.setCustCare(custCare);
            branch.setPhone(phone);
            branch.setState(state);
            branch.setDistrict(district);
            branch.setAddress(address);
            branch.setPinCode(pinCode);
            branch.setName(name);
            branch.setId(ifsc);
            branch.setCity(city);
            branch.setBranchCode(branchCode);
            return branchOfy.loadByKey(branchOfy.save(branch));
        }
        return null;
    }


    @ApiMethod(name = "admin.importBranches")
    public List<Branch> importBranches(@Named("bucket") String bucket,
                                       @Named("fileName") String fileName, User user) {
        if (Auth.validate(user)) {
            BulkUpload bulkUpload = new BulkUpload();
            bulkUpload.setBucket(bucket);
            bulkUpload.setFileName(fileName);
            return bulkUpload.importBranch();
        }
        return null;
    }

    @ApiMethod(name = "public.branches")
    public List<Branch> branches(
            @Named("bankName") String bankName,
            @Named("stateName") String stateName,
            @Named("districtName") String districtName) {
        return branchOfy.branches(Word.capitalize(bankName), stateName, districtName);
    }

    @ApiMethod(name = "public.banks")
    public List<Bank> banks() {
        return bankOfy.listAll();
    }

    /**
     * List of states by bank name.
     *
     * @param bankName
     * @return List<String>
     */
    @ApiMethod(name = "public.states")
    public List<Branch> states(@Named("bankName") String bankName) {
        return branchOfy.statesList(Word.capitalize(bankName));
    }

    /**
     * List of districts by bank and state name.
     *
     * @param bankName
     * @param stateName
     * @return List<String>
     */
    @ApiMethod(name = "public.districts")
    public List<Branch> districts(@Named("bankName") String bankName,
                                  @Named("stateName") String stateName) {
        return branchOfy.districtsList(Word.capitalize(bankName), stateName);
    }

    @ApiMethod(name = "admin.createGcsFile")
    public List<String> createGcsFile(@Named("fileName") String fileName, @Named("content") String content, User user) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(content);
        return new SiteMap().createFile(fileName, strBuilder);
    }

    @ApiMethod(name = "admin.createSiteMap")
    public List<String> createSiteMap(User user) {
        if (Auth.validate(user)) {
            return new SiteMap().createSiteMap();
        }
        return null;
    }

    @ApiMethod(name = "admin.createSiteMapIndex")
    public List<String> createSiteMapIndex(User user) {
        if (Auth.validate(user)) {
            return new SiteMap().createSiteMapIndex();
        }
        return null;
    }

    @ApiMethod(name = "public.feedback")
    public Feedback feedback(@Named("feedback") String feedback, @Nullable @Named("email") String email) {
        FeedbackOfy ff = new FeedbackOfy();
        Feedback f = new Feedback();
        f.setFeedback(feedback);
        if (email != null) {
            f.setEmail(new Email(email));
        }
        return ff.loadByKey(ff.save(f));
    }

    @ApiMethod(name = "public.branchByIFSC")
    public List<Branch> branchIfsc(@Named("ifsc") String ifsc) {
        return branchOfy.loadByIFSC(ifsc.trim());
    }

    @ApiMethod(name = "admin.addAdmin")
    public Admin addAdmin(@Named("name") String name, @Named("email") String email, User user) {
        if (Auth.validate(user)) {
            Admin admin = new Admin();
            AdminOfy adminOfy = new AdminOfy();
            admin.setName(name);
            admin.setEmail(email);
            return adminOfy.loadByKey(adminOfy.save(admin));
        }
        logger.warning("Invalid User = user" + user);
        return null;

    }

    @ApiMethod(name = "admin.adminByEmail")
    public List<Admin> adminByEmail(@Named("email") String email) {
        AdminOfy adminOfy = new AdminOfy();
        return adminOfy.loadByEmail(email);
    }

    @ApiMethod(name = "public.search")
    public Map search(@Nullable @Named("bankName") String bankName,
                      @Named("query") String query,
                      @Nullable @Named("cursor") String cursor) {

        return BranchSearch.search(bankName, query, cursor);
    }

    @ApiMethod(name = "admin.addBank")
    public Bank addBank(@Named("name") String name,
                        @Named("image") String image,
                        @Named("state") String state,
                        @Named("district") String district,
                        @Named("city") String city,
                        @Named("address") String address,
                        @Named("email") String email,
                        @Named("phone") String phone,
                        @Named("mobile") String mobile,
                        @Named("pinCode") String pinCode,
                        User user
    ) throws MalformedURLException {
        if (Auth.validate(user)) {
            Bank bank = BankOfy.loadByName(WordUtils.capitalizeFully(name).trim());
            if (bank != null) {
                bank.setUpdateDate(new Date());
            } else {
                bank = new Bank();
                bank.setAddDate(new Date());
            }
            bank.setId(name);
            bank.setName(name);
            bank.setImage(new URL(image));
            bank.setState(state);
            bank.setDistrict(district);
            bank.setCity(city);
            bank.setAddress(address);
            if (email != null && !email.isEmpty()) {
                bank.setEmail(new Email(email));
            }
            bank.setMobile(mobile);
            bank.setPhone(phone);
            bank.setPinCode(pinCode);
            bankOfy.loadByKey(bankOfy.save(bank));
            return bankOfy.loadByKey(bankOfy.save(bank));

        }
        return null;

    }
}
