package edu.cmu.bap.client;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import edu.cmu.bap.client.Symbol;

public class Image extends Resource {
	private final String filename;
	private List<Segment> segments = null;
	// private final Symbols<Segment> symbols = null;
	
	
	Image(String filename, int resourceId) {
		// set resource Id.
		super(resourceId, BapClient.getInstance().getResource(resourceId).getJSONObject("image"));
		this.filename = filename;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public List<Segment> getSegments() {
		if (segments == null) {
			// now load them.
			// get list of the segments
			JSONArray array = getResource().getJSONArray("segments");
			System.out.println("Size: " + array.length());
			// call getResource on each of the ids
			// create Segment objects and add to this list
			segments = new LinkedList<Segment>();
			for (int i = 0; i < array.length(); i++) {
				segments.add(new Segment(array.getInt(i)));
			}
		}
		return segments;
	}
	
	public List<Symbol> getSymbols () {
		List<Symbol> symbols = new LinkedList<Symbol>(); // TODO store local copy?
		for (Segment seg : getSegments()) {
			for (Symbol sym : seg.getSymbols()) {
				symbols.add(sym);
			}
		}
		
		return symbols;
	}
}