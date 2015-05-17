package bnifsc;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import bnifsc.entites.Branch;
import bnifsc.entites.Bank;
import bnifsc.entites.Feedback;
import bnifsc.util.BulkUpload;
import bnifsc.util.SiteMap;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;

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
    public Branch addBank(@Named("name") String name,
                          @Named("branchName") String branchName, @Named("ifsc") String ifsc,
                          @Named("micr") String micr, @Named("swift") String swift,
                          @Named("email") String email, @Named("mobile") String mobile,
                          @Named("customerCare") String custCare,
                          @Named("phone") String phone, @Named("state") String state,
                          @Named("district") String district,
                          @Named("address") String address, @Named("pin") String pincode, User user) {
        if (Auth.validate(user)) {
            Branch bank = new Branch();
            bank.setName(name);
            bank.setBranchName(branchName);
            bank.setIfsc(ifsc);
            bank.setMicr(micr);
            bank.setSwift(swift);
            bank.setEmail(email);
            bank.setMobile(mobile);
            bank.setCustCare(custCare);
            bank.setPhone(phone);
            bank.setState(state);
            bank.setDistrict(district);
            bank.setAddress(address);
            bank.setPincode(pincode);
            return bank.save();
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
    public List<Map<String, String>> branches(
            @Named("bankName") String bankName,
            @Named("stateName") String stateName,
            @Named("districtName") String districtName) {
        Branch branch = new Branch();
        branch.setName(bankName);
        branch.setDistrict(districtName);
        branch.setState(stateName);
        return branch.branches();
    }

    @ApiMethod(name = "public.branchByKey")
    public Branch getBranchByKey(@Named("keyString") String keyString) {
        return new Branch().getBranchByKey(keyString);
    }

    @ApiMethod(name = "public.banks")
    public List<String> banks() {
        Branch branch = new Branch();
        return branch.banks();
    }

    @ApiMethod(name = "public.states")
    public List<String> states(@Named("bankName") String bankName) {
        Branch branch = new Branch();
        branch.setName(bankName);
        return branch.states();
    }

    @ApiMethod(name = "public.districts")
    public List<String> districts(@Named("bankName") String bankName,
                                  @Named("stateName") String stateName) {
        Branch branch = new Branch();
        branch.setName(bankName);
        branch.setState(stateName);
        return branch.districts();
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
    public Feedback feedback(@Named("feedback") String feedback) {
        return new Feedback(feedback).save();
    }

    @ApiMethod(name = "public.branchByIFSC")
    public Branch branchIfsc(@Named("IFSC code") String ifsc) {
        return Branch.branchByIfsc(ifsc);
    }
}
