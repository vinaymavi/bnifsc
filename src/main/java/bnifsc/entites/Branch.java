package bnifsc.entites;

import java.net.URLDecoder;
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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
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

	public Branch getBranchByKey(String keyString){
		try{
				Entity entity = datastore.get(KeyFactory.stringToKey(keyString));
				Branch branch = new Branch();
				branch.setName((String)entity.getProperty("name"));
				branch.setState((String)entity.getProperty("state"));
				branch.setDistrict((String)entity.getProperty("district"));
				branch.setBranchName((String)entity.getProperty("branchname"));
				branch.setCustCare((String)entity.getProperty("custCare"));
				branch.setEmail((String)entity.getProperty("email"));
				branch.setMobile((String)entity.getProperty("mobile"));
				branch.setPhone((String)entity.getProperty("phone"));
				branch.setAddress((String)entity.getProperty("address"));
				branch.setIfsc((String)entity.getProperty("ifsc"));
				branch.setMicr((String)entity.getProperty("micr"));
				branch.setSwift((String)entity.getProperty("swift"));
				branch.setPincode((String)entity.getProperty("pincode"));
				return branch;
			}catch(EntityNotFoundException e){
			logger.warning(e.getMessage());
		}
		return null;
	}
	
	public List<Map<String, String>> branches() {
		Query query = new Query(ENTITY_NAME);
		Filter bankFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				this.getName());
		Filter stateFilter = new FilterPredicate("state", FilterOperator.EQUAL,
				this.getState());
		 Filter districtFilter = new FilterPredicate("district",
		 FilterOperator.EQUAL, this.getDistrict());
		Filter bankAndStateFilter = CompositeFilterOperator.and(bankFilter,stateFilter,districtFilter);
		query.setFilter(bankAndStateFilter);
		query.addProjection(new PropertyProjection("branchname", String.class));
		PreparedQuery pq = datastore.prepare(query);
		List<Map<String, String>> branches = new ArrayList<Map<String, String>>();
		for (Entity e : pq.asIterable()) {
			Map<String,String> b = new HashMap<String,String>();
			b.put("branchName", (String)e.getProperty("branchname"));
			b.put("id", KeyFactory.keyToString(e.getKey()));
			branches.add(b);
		}
		logger.warning(this.getName() + "," + this.getState() + ","
				+ this.getDistrict());
		logger.warning("Branches List size=" + branches.size());
		return branches;
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
		logger.warning("Banks list size=" + bankNames.size());
		return bankNames;
	}

	public List<String> states() {
		Query query = new Query(ENTITY_NAME);
		Filter bankFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				this.getName());
		query.setFilter(bankFilter);
		query.addProjection(new PropertyProjection("state", String.class));
		query.setDistinct(true);
		PreparedQuery pq = datastore.prepare(query);
		List<String> states = new ArrayList<String>();
		for (Entity entity : pq.asIterable()) {
			states.add((String) entity.getProperty("state"));
		}
		logger.warning("State List size=" + states.size());
		return states;
	}

	public List<String> districts() {
		Query query = new Query(ENTITY_NAME);
		Filter bankFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				this.getName());
		Filter stateFilter = new FilterPredicate("state", FilterOperator.EQUAL,
				this.getState());
		Filter bankAndStateFilter = CompositeFilterOperator.and(bankFilter,
				stateFilter);
		query.setFilter(bankAndStateFilter);
		query.addProjection(new PropertyProjection("district", String.class));
		query.setDistinct(true);
		PreparedQuery pq = datastore.prepare(query);
		List<String> districts = new ArrayList<String>();
		for (Entity e : pq.asIterable()) {
			districts.add((String) e.getProperty("district"));
		}
		logger.warning("District list size=" + districts.size());
		return districts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = URLDecoder.decode(name);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = URLDecoder.decode(state);
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = URLDecoder.decode(district);
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = URLDecoder.decode(branchName);
	}

	public String getCustCare() {
		return custCare;
	}

	public void setCustCare(String custCare) {
		this.custCare = URLDecoder.decode(custCare);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = URLDecoder.decode(email);
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
		this.address = URLDecoder.decode(address);
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = URLDecoder.decode(ifsc);
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
