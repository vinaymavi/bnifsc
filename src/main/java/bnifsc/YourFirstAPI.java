package bnifsc;

import bnifsc.entites.Bank;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;
/** An endpoint class we are exposing */
@Api(name = "bnifsc",
     version = "v1",
     scopes = {Constants.EMAIL_SCOPE},
     clientIds = {Constants.WEB_CLIENT_ID},    		    
     namespace = @ApiNamespace(ownerDomain = "helloworld.example.com",
                                ownerName = "helloworld.example.com",
                                packagePath=""))
public class YourFirstAPI {
    /** Add Bank details*/
    @ApiMethod(name="addBank")
    public Bank addBank(@Named("name") String name,@Named("branchName") String branchName,@Named("ifsc") String ifsc,
    					@Named("micr") String micr, @Named("swift") String swift,@Named("email") String email,
    					@Named("mobile") String mobile,@Named("customerCare") String custCare,@Named("phone") String phone,
    					@Named("state") String state,@Named("district") String district,@Named("address") String address,
    					@Named("pin") String pincode){    	
    	Bank bank = new Bank();
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
    	return  bank.save();
    	
    }    
}
