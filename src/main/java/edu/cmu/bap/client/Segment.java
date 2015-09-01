package edu.cmu.bap.client;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

public class Segment extends Resource {
	private List<Symbol> symbols = null;

	Segment(int resourceId) {
		super(resourceId, BapClient.getInstance().getResource(resourceId).getJSONObject("segment"));
	}
	
	public List<Symbol> getSymbols() {
		if (symbols == null) {
			JSONArray array = getResource().getJSONArray("symbols");
			System.out.println("Size: " + array.length());
			// call getResource on each of the ids
			// create Segment objects and add to this list
			symbols = new LinkedList<Symbol>();
			for (int i = 0; i < array.length(); i++) {
				symbols.add(new Symbol(array.getInt(i)));
			}
		}
		return symbols;
	}

}
