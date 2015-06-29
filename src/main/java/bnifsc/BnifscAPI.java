package bnifsc;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import bnifsc.entites.Admin;
import bnifsc.entites.Branch;
import bnifsc.entites.Bank;
import bnifsc.entites.Feedback;
import bnifsc.util.BulkUpload;
import bnifsc.util.SiteMap;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.users.User;
import persist.AdminOfy;
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
//    TODO make multi file api to avoid writing public function 2 times.
    /**
     * Add Bank details
     */
    private final static Logger logger = Logger.getLogger(BnifscAPI.class
            .getName());

    //TODO rename ApiMethod name with admin,user.public namespace.
    @ApiMethod(name = "admin.addBank")
    public Branch addBank(@Named("name") String bankName,
                          @Named("branchName") String branchName, @Named("ifsc") String ifsc,
                          @Named("micr") String micr, @Named("swift") String swift,
                          @Named("email") String email, @Named("mobile") String mobile,
                          @Named("customerCare") String custCare,
                          @Named("phone") String phone, @Named("state") String state,
                          @Named("district") String district,
                          @Named("address") String address, @Named("pin") String pincode, User user) {
        if (Auth.validate(user)) {
            Branch branch = new Branch();
            BranchOfy branchOfy = new BranchOfy();
            branch.setBankName(bankName);
            branch.setBranchName(branchName);
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
            branch.setPinCode(pincode);
            return branchOfy.loadByKey(branchOfy.save(branch));
        }
        return null;
    }

    @ApiMethod(name = "admin.importBankNames")
    public List<Bank> importBankNames(@Named("bucket") String bucket,
                                      @Named("fileName") String fileName, User user) {
        if (Auth.validate(user)) {
            BulkUpload bulkUpload = new BulkUpload();
            bulkUpload.setBucket(bucket);
            bulkUpload.setFileName(fileName);
            return bulkUpload.importBankNames();
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
        BranchOfy branchOfy = new BranchOfy();
        return branchOfy.branches(bankName, stateName, districtName);
    }

    @ApiMethod(name = "public.bankNamesList")
    public List<Branch> banks() {
        BranchOfy branchOfy = new BranchOfy();
        return branchOfy.banksList();
    }

    /**
     * List of states by bank name.
     *
     * @param bankName
     * @return List<String>
     */
    @ApiMethod(name = "public.states")
    public List<Branch> states(@Named("bankName") String bankName) {
        BranchOfy branchOfy = new BranchOfy();
        return branchOfy.statesList(bankName);
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
        BranchOfy branchOfy = new BranchOfy();
        return branchOfy.districtsList(bankName, stateName);
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
        BranchOfy branchOfy = new BranchOfy();
        return branchOfy.loadByIFSC(ifsc);
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
        return null;

    }

    @ApiMethod(name = "admin.adminByEmail")
    public List<Admin> adminByEmail(@Named("email") String email) {
        AdminOfy adminOfy = new AdminOfy();
        return adminOfy.loadByEmail(email);
    }
}
