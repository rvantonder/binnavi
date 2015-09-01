package edu.cmu.bap.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

	int lastId;

	public static BapClient getInstance() {
		if (bapClientInstance == null) { // TODO implement singleton with enum
			bapClientInstance = new BapClient();
		}
		return bapClientInstance;
	}

	private BapClient() {
		lastId = 0;
		try {
			threadPool = Executors.newCachedThreadPool();
			bapServerUrl = new URL(PROTO, HOST, PORT, "");
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO private?
	public JSONObject asyncRequestTask(JSONObject request) {
		DataOutputStream out;
		BufferedReader in;
		HttpURLConnection conn;

		JSONObject jsonResult = null;

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

			jsonResult = new JSONObject(result.toString());
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject asyncRequest(JSONObject request) {
		CompletableFuture<JSONObject> f = CompletableFuture.supplyAsync(() -> asyncRequestTask(request), threadPool);

		JSONObject res = null;
		try {
			res = f.get();
		}
		catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// API start

	public JSONObject init() {
		JSONObject init = new JSONObject();
		JSONObject version = new JSONObject();
		version.put("version", "0.1");
		init.put("init", version);
		lastId += 1;
		init.put("id", Integer.toString(lastId));
		return asyncRequest(init);
	}

	public JSONObject loadFile(String fileName) {
		JSONObject loadFileRequest = new JSONObject();
		JSONObject url = new JSONObject();
		loadFileRequest.put("load_file", url.put("url", "file://" + fileName));
		lastId += 1;
		loadFileRequest.put("id", Integer.toString(lastId));

		return asyncRequest(loadFileRequest);
	}

	public JSONObject getResource(int resourceId) {
		JSONObject getResource = new JSONObject();
		getResource.put("get_resource", Integer.toString(resourceId));
		lastId += 1;
		getResource.put("id", Integer.toString(lastId));
		return asyncRequest(getResource);
	}

	public Image getImage(String fileName) {
		JSONObject res = loadFile(fileName);
		return new Image(fileName, Integer.parseInt(res.getString("resource")));
	}
}
