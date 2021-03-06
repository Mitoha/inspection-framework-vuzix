package com.teamproject.inspectionframework.Application_Layer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.os.StrictMode;
import android.util.Log;

/**
 * Provides functions for communication between client and server (REST
 * Services)
 *
 */
public class HttpCustomClient {

	// VAR-declaration
	public HttpClient client = new DefaultHttpClient();
	public CookieStore store = ((DefaultHttpClient) client).getCookieStore();

	public HttpCustomClient() {
	}

	/**
	 * Reads a specific source on the server
	 * 
	 * @param uri
	 *            URI for the type of resource that should be read
	 * @return String read from the server
	 */
	public String readHerokuServer(String uri) {

		// Allow internet access
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// String-Builder for output String
		StringBuilder builder = new StringBuilder();
		// Get the cookie from the Cookiestore
		HttpContext ctx = new BasicHttpContext();
		ctx.setAttribute(ClientContext.COOKIE_STORE, store);
		// set the Url for the Get-request
		HttpGet httpGet = new HttpGet("http://inspection-framework.herokuapp.com/" + uri);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			System.out.println("GET:" + statusCode);
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));

				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				content.close();
			} else {
				Log.e(ParseJSON.class.toString(), "Download not possible!");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(builder.toString());
		return builder.toString();

	}

	/**
	 * Posts a new objects to the server (not stored on the server DB so far)
	 * 
	 * @param uri
	 *            Location on the server where the string should be posted
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 * @return The status (true = successful / false = not successful)
	 */
	public boolean postToHerokuServer(String uri, String username, String password) {
		// Var declaration
		boolean status = false;
		StringBuilder builder = new StringBuilder();
		// Allow internet connection
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// Set URL for post-request
		HttpPost httpPost = new HttpPost("http://inspection-framework.herokuapp.com/" + uri);

		try {
			List<NameValuePair> params = new LinkedList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			System.out.println("POST:" + statusCode);
			System.out.println("POST Response:" + response);
			if (statusCode == 200) {
				status = true;

				List<Cookie> cookies = store.getCookies();

				if (cookies != null) {
					for (Cookie c : cookies) {
						store.addCookie(c);
						System.out.println(c);
					}
				}

				HttpEntity entity = response.getEntity();

				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				content.close();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				entity.consumeContent();
			} else {
				Log.e(ParseJSON.class.toString(), "Download not possible!");
				status = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return status;
	}

	/**
	 * Posts an attachment as a byte array to the server
	 * 
	 * @param assignmentId
	 *            ID of the corresponding assignment
	 * @param taskId
	 *            ID of the corresponding task
	 * @param byteArray
	 *            Byte array of the image that is sent to the server
	 */
	public void postAttachmentToHerokuServer(String assignmentId, String taskId, byte[] byteArray, String type) {
		// declaration
		StringBuilder stringBuilder = new StringBuilder();
		InputStreamBody inputStreamBody = null;

		// Allow internet connection
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String URL = "http://inspection-framework.herokuapp.com/assignment" + "/" + assignmentId + "/" + "task" + "/" + taskId + "/" + "attachment";
		// Set URL for post-request
		HttpEntityEnclosingRequestBase httpPost = new HttpPost(URL);

		// creates a new MultipartEntity and sets the browser policy
		MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
		multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		// Create a new inputStreamBody and add the bytearray(picture) to it
		// Give names for the picture
		if (type.equals("Photo")) {
			inputStreamBody = new InputStreamBody(new ByteArrayInputStream(byteArray), "Pic.jpg");

		}
		if (type.equals("Audio")) {
			inputStreamBody = new InputStreamBody(new ByteArrayInputStream(byteArray), "Audio.3gp");
		}
		// Add the filebody to the multipartEntity
		// Specified from serverside it must be "fileUpload"
		multipartEntity.addPart("fileUpload", inputStreamBody);

		try {

			httpPost.setEntity(multipartEntity.build());
			HttpResponse response = client.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			System.out.println(statusCode);
			System.out.println(response);

			// If everything is ok
			if (statusCode == 200) {

				System.out.println("Upload succesfull");
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
					System.out.println(line);
				}
//				client.getConnectionManager().shutdown();
			} else {
				Log.e(ParseJSON.class.toString(), "Upload not possible!");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update an already existing object on the server's database
	 * 
	 * @param uri
	 *            Location where the objects should be put
	 * @param jsonObject
	 *            The object that is sent to the server
	 * @param Id
	 *            The object's ID
	 * @return The response code from the server
	 */
	public Integer putToHerokuServer(String uri, String jsonObject, String Id) {
		HttpResponse response1 = null;

		// Allow internet connection
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// set URL for Put-request
		HttpPut httpPut = new HttpPut("http://inspection-framework.herokuapp.com/" + uri + "/" + Id);

		StringEntity se = null;
		try {
			se = new StringEntity(jsonObject, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// sets a request header so the page receving the request
		// will know what to do with it

		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");

		client.getConnectionManager().closeExpiredConnections();
		httpPut.setEntity(se);

		try {
			response1 = client.execute(httpPut);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response1);
		return (Integer) response1.getStatusLine().getStatusCode();
	}

}