package edu.cmu.bap.client;

import org.json.JSONArray;
import org.json.JSONObject;

public class InsnsParser {
	
	// TODO, improve return type
	public static String parseBil(JSONArray insns) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < insns.length(); i++) {
			sb.append("{\n");
			JSONObject insn = insns.getJSONObject(i);
			JSONArray bilInsns = insn.getJSONArray("bil");
			for (int j = 0; j < bilInsns.length(); j++) {
				sb.append("\t"+bilInsns.get(j)+"\n");
			}
			sb.append("}\n");
		}
		
		return sb.toString();
	}

}
