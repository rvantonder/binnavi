package edu.cmu.bap.client;

import org.json.JSONArray;
import org.json.JSONObject;

public class Resource {
	private final int id;
	private final JSONObject resource;
	
	Resource(int id, JSONObject resource) {
		this.id = id;
		this.resource = resource;
	}
	
	public JSONObject getResource() {
		return resource;
	}
	
	public int getResourceId() {
		return this.id;
	}
	
	public String toString() {
		return resource.toString();
	}
	
	// Array of JSONObjects, each with a 'name', 'asm', 'bil, and so on
	public JSONArray getInsns() {
		return BapClient.getInstance()
				.getInsns(getResourceId())
				.getJSONArray("insns");
	}
}