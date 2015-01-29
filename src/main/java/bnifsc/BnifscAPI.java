package bnifsc;

import java.util.List;
import java.util.logging.Logger;

import bnifsc.entites.Branch;
import bnifsc.entites.Bank;
import bnifsc.util.BulkUpload;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.Entity;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(name = "bnifsc", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID }, namespace = @ApiNamespace(ownerDomain = "helloworld.example.com", ownerName = "helloworld.example.com", packagePath = ""))
public class BnifscAPI {
	/** Add Bank details */
	private final static Logger logger = Logger.getLogger(BnifscAPI.class
			.getName());

	@ApiMethod(name = "addBank")
	public Branch addBank(@Named("name") String name,
			@Named("branchName") String branchName, @Named("ifsc") String ifsc,
			@Named("micr") String micr, @Named("swift") String swift,
			@Named("email") String email, @Named("mobile") String mobile,
			@Named("customerCare") String custCare,
			@Named("phone") String phone, @Named("state") String state,
			@Named("district") String district,
			@Named("address") String address, @Named("pin") String pincode) {
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

	@ApiMethod(name = "importBankNames")
	public List<Bank> importBankNames(@Named("bucket") String bucket,
			@Named("fileName") String fileName) {
		BulkUpload bulkUpload = new BulkUpload();
		bulkUpload.setBucket(bucket);
		bulkUpload.setFileName(fileName);		
		return bulkUpload.importBankNames();
	}

	@ApiMethod(name = "importBranches")
	public List<Branch> importBranches(@Named("bucket") String bucket,
			@Named("fileName") String fileName) {
		BulkUpload bulkUpload = new BulkUpload();
		bulkUpload.setBucket(bucket);
		bulkUpload.setFileName(fileName);		
		return bulkUpload.importBranch();
		
	}
	@ApiMethod(name="branches")
	public List<Entity> branches(@Named("limit") int limit){
		Branch branch = new Branch();
		return branch.branches(limit);		
	}
	
	@ApiMethod(name="banks")
	public List<Entity> banks(){
		Branch branch = new Branch();
		return branch.banks();
	}	
}
