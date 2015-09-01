package edu.cmu.bap.client;

public class Image extends Resource {
	private final String fileName;
	
	Image(String fileName, int id) {
		// get resource id
		super(id);
		this.fileName = fileName;
		//BapClient.getInstance().loadFile(fileName);
		// now i have the id. construct resource
	}
	
	public String getFileName() {
		return this.fileName;
	}
}