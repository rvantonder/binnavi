package edu.cmu.bap.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;



public class Init {
	
	public static void sendInitRequest() {
		String data = "{\"init\": {\"version\": \"0.1\"}, \"id\": \"1\"}";
		byte[] postData = data.getBytes(StandardCharsets.UTF_8);
		String host = "http://127.0.0.1:4444";
		try {
			URL    url            = new URL( host );
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();    
			conn.setDoOutput( true );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Type", "application/json");
			conn.setRequestProperty( "charset", "utf-8");
			DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
			wr.write(postData);
			 Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			 for ( int c = in.read(); c != -1; c = in.read() )
		            System.out.print((char)c);
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
