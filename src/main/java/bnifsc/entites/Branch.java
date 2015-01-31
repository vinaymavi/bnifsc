package bnifsc.entites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import bnifsc.util.BulkUpload;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;

public class Branch {
	public static String ENTITY_NAME = "Bank";
	public static final int DEFAULT_LIMIT = 20;
	private final static Logger logger = Logger.getLogger(BulkUpload.class
			.getName());
	private final static DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();
	private final static Gson GSON = new Gson();
	private String name; // Bank name
	private String state;
	private String district;
	private String branchName;
	private String custCare;
	private String email;
	private String mobile;
	private String phone;
	private String address;
	private String ifsc;
	private String micr;
	private String swift;
	private String pincode;
	private MongoKey _id;

	// Save bank to datastore.
	// TODO this function should return Bank object.
	public Branch save() {

		Entity bank = new Entity(ENTITY_NAME);
		bank.setProperty("name", this.getName());
		bank.setProperty("state", this.getState());
		bank.setProperty("district", this.getDistrict());
		bank.setProperty("branchname", this.getBranchName());
		bank.setProperty("custCare", this.getCustCare());
		bank.setProperty("email", this.getEmail());
		bank.setProperty("mobile", this.getMobile());
		bank.setProperty("phone", this.getPhone());
		bank.setProperty("address", this.getAddress());
		bank.setProperty("ifsc", this.getIfsc());
		bank.setProperty("micr", this.getMicr());
		bank.setProperty("swift", this.getSwift());
		bank.setProperty("pincode", this.getPincode());
		datastore.put(bank);
		return this;
	}

	public List<Entity> branches(int limit) {
		Query query = new Query(ENTITY_NAME);
		PreparedQuery pq = datastore.prepare(query);
		return pq.asList(FetchOptions.Builder.withLimit(limit));
	}

	public List<String> banks() {
		Query query = new Query(ENTITY_NAME);
		query.addProjection(new PropertyProjection("name", String.class));
		query.setDistinct(true);
		PreparedQuery pq = datastore.prepare(query);
		List<String> bankNames = new ArrayList<String>();
		for (Entity entity : pq.asIterable()) {
			bankNames.add((String) entity.getProperty("name"));
		}
		logger.warning("Banks list size="+bankNames.size());
		return bankNames;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public MongoKey get_id() {
		return _id;
	}

	public void set_id(MongoKey _id) {
		this._id = _id;
	}

}
