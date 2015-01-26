package bnifsc.entites;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


public class Bank {
	private static final String ENTITY_NAME="BankNames"; 
	private String name;
	private String url;
	private MongoKey _id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MongoKey get_id() {
		return _id;
	}

	public void set_id(MongoKey _id) {
		this._id = _id;
	}
	
	public Bank save(){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Entity entity= new Entity(ENTITY_NAME);
		entity.setProperty("name", this.getName());
		entity.setProperty("url", this.getUrl());
		datastoreService.put(entity);
		return this;
	}
}
