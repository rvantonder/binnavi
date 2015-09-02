package edu.cmu.bap.client;

public class Symbol extends Resource {

	Symbol(int resourceId) {
		super(resourceId, BapClient.getInstance().getResource(resourceId).getJSONObject("symbol"));
	}
}
