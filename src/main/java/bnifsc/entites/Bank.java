package bnifsc.entites;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


public class Bank {
	private static final String ENTITY_NAME="BankNames";
	private static final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
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
		Entity entity= new Entity(ENTITY_NAME);
		entity.setProperty("name", this.getName());
		entity.setProperty("url", this.getUrl());
		datastoreService.put(entity);
		return this;
	}
	public List<Entity> banks(){
		Query query = new Query(ENTITY_NAME);
		PreparedQuery pq = datastoreService.prepare(query);
		List<Entity> enityList = new ArrayList<Entity>();
		for(Entity entity:pq.asIterable()){
			enityList.add(entity);			
		}		
		return enityList;
	}
}
