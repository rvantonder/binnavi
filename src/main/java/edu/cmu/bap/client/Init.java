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
	
	protected static String port = "4444";
	protected static String host = "http://127.0.0.1";
	protected static String method = "POST";
	
	public static void sendInitRequest() {
		String data = "{\"init\": {\"version\": \"0.1\"}, \"id\": \"1\"}";
		byte[] postData = data.getBytes(StandardCharsets.UTF_8);
		try {
			URL    url            = new URL(String.format("%s:%s", host, port));
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();    
			conn.setDoOutput( true );
			conn.setRequestMethod(method);
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
	
	//curl --data "{\"load_file\": {\"url\": \"file:///home/vagrant/coreutils_O0_ls\"}, \"id\": \"25\"}" localhost:8080
	// resp: {"id":"25","resource":"7955"}
	// curl --data "{\"get_resource\": \"7955\", \"id\": \"25\"}" localhost:8080
	// resp: {"id":"25","image":{"links":["mmap:/home/vagrant/coreutils_O0_ls?q=7955&length=337449&offset=0"],"arch":"armv7","entry_point":"40600","addr_size":"32","endian":"LittleEndian()","segments":["8805","7956"]}}
	// 
	
	public static void processImageRequest() {
		String data = "{\"id\": \"3\", \"load_file\": {\"url\": \"file:///vagrant/coreutils_O0_ls\"}}";
		byte[] postData = data.getBytes(StandardCharsets.UTF_8);
		try {
			URL    url            = new URL(String.format("%s:%s", host, port));
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();    
			conn.setDoOutput( true );
			conn.setRequestMethod(method);
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
