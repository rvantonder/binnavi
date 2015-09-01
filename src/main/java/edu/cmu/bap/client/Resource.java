package edu.cmu.bap.client;

import org.json.JSONObject;

public class Resource {
	private final int id;
	
	Resource(int id) {
		this.id = id;
	}
	
	public JSONObject getResource() {
		return BapClient.getInstance().getResource(this.id);
	}
	
	public int getResourceId() {
		return this.id;
	}
	
}