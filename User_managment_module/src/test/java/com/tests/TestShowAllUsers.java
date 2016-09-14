package com.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.roomautomation.dao.UserDao;
import com.roomautomation.pojo.User;

public class TestShowAllUsers {

	static InputStream inputStream = null;
	static JSONObject jsonObj = null;
	static String json = "";
	static String line = null;
	final static Logger logger = Logger.getLogger(TestShowAllUsers.class);

	// constructor
	public TestShowAllUsers() {

	}

	// method to cal post rest api's
	public String callURL(String myURL) {

		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(myURL);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();

		} catch (IOException e) {
			logger.error("IOException in test case file" + e.getMessage());

		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			line = reader.readLine();
			System.out.println(line);
			inputStream.close();
		} catch (Exception e) {
			logger.error("conversion error for data  in test case file" + e.getMessage());
		}

		return line;

	}

	// method to cal get rest api's
	public JSONObject getJSONFromUrl(String url) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();

		} catch (IOException e) {
			logger.error("IOException in test case file" + e.getMessage());

		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			inputStream.close();
			json = sb.toString();
			// json = json.substring(1, (json.length() - 1));

		} catch (Exception e) {
			logger.error("conversion error for data  in test case file" + e.getMessage());
		}

		// try parse the string to a JSON object
		try {
			jsonObj = new JSONObject(json);
		} catch (JSONException e) {
			logger.error("JSONException  in test case file" + e.getMessage());
		}

		// return JSON String
		return jsonObj;

	}

	@Test
	public void testShowAllUsersMethod() {
		TestShowAllUsers test = new TestShowAllUsers();
		assertEquals(test.getJSONFromUrl("http://localhost:8080/User_managment_module/project/showAllUsers"), line);

	}

	@Test
	public void testAddUserMethod() {

		TestShowAllUsers test = new TestShowAllUsers();
		assertEquals(test.callURL("http://localhost:8080/User_managment_module/project/addUser/test/ters/test"),
				"success");
	}

	@Test
	public void testDeleteUserMethod() {

		TestShowAllUsers test = new TestShowAllUsers();
		assertEquals(test.callURL("http://localhost:8080/User_managment_module/project/deleteUser/test/2"), "success");
	}

	@Test
	public void testUpdateUserMethod() {

		TestShowAllUsers test = new TestShowAllUsers();
		assertEquals(test.callURL("http://localhost:8080/User_managment_module/project/updateUser/kp/kp/test/2/admin"),
				"success");
	}

}
