package edu.cmu.bap.client;

import java.util.List;

public class Symbol extends Resource {

	Symbol(int resourceId) {
		super(resourceId, BapClient.getInstance().getResource(resourceId).getJSONObject("symbol"));
	}

}
