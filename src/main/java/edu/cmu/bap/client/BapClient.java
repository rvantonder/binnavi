package edu.cmu.bap.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

public class BapClient {
	// TODO parameterize constructor
	public static final String HOST = "127.0.0.1";

	public static final int PORT = 4444;

	public static final String PROTO = "http";

	public static final String HTTP_METHOD_POST = "POST";

	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

	public static final String HTTP_CONTENT_TYPE_JSON = "application/json";

	public static final String HTTP_CHARSET_UTF8 = "utf-8";

	URL bapServerUrl;

	private static BapClient bapClientInstance;

	private ExecutorService threadPool;

	public static BapClient getInstance() {
		if (bapClientInstance == null) { // TODO implement singleton with enum
			bapClientInstance = new BapClient();
		}
		return bapClientInstance;
	}

	private BapClient() {
		try {
			threadPool = Executors.newCachedThreadPool();
			bapServerUrl = new URL(PROTO, HOST, PORT, "");
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class HttpPostTask implements Runnable {
		private JSONObject request;

		private HttpURLConnection conn;

		public HttpPostTask(JSONObject request) {
			this.request = request;
		}

		@Override
		public void run() {
			DataOutputStream out;
			BufferedReader in;

			try {
				conn = (HttpURLConnection) bapServerUrl.openConnection();

				conn.setDoOutput(true);
				conn.setRequestMethod(HTTP_METHOD_POST);
				conn.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, HTTP_CONTENT_TYPE_JSON);
				conn.setRequestProperty("charset", HTTP_CHARSET_UTF8);
				out = new DataOutputStream(conn.getOutputStream());
				System.out.println("Writing " + request.toString());
				out.write(request.toString().getBytes());

				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), HTTP_CHARSET_UTF8));
				System.out.println("Output start");
				
				StringBuilder result = new StringBuilder();
				String line = "";
				while ((line = in.readLine()) != null) {
					System.out.println(line);
					result.append(line);
				}
				in.close();
				out.close();
				
				JSONObject jsonResult = new JSONObject(result.toString());
				handleResponse("init", jsonResult);
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
	
	private void handleResponse(String s, JSONObject jsonResult) {
		switch (s) {
		case "init":
			System.out.println("Capabilities: " + jsonResult.getString("id"));
		}
	}

	public void init() {
		JSONObject init = new JSONObject();
		JSONObject version = new JSONObject();
		version.put("version", "0.1");
		init.put("init", version);
		init.put("id", "1");
		threadPool.execute(new HttpPostTask(init));
	}
}
