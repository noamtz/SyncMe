package com.example.syncme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.util.Log;

public class ServerUtils {

	public static String SERVER_URL = "http://10.0.0.1/SyncMe/SyncMe.Server/syncMeApp.php";
	public static String POST = "post";

	public static String register(String name, String email, String regId, String server){

		Map<String, String> params = new HashMap<String, String>();
		JSONObject user = new JSONObject();
		String res = "";

		try{
			user.put("firstname", name);
			user.put("lastname", name);
			user.put("email", email);
			user.put("regId", regId);

			params.put("method", "register");
			params.put("params", user.toString());
				
			res = executeHTTP(POST, SERVER_URL , params);

		}catch(JSONException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

		return res.contentEquals("succeeded") ? "Registration succeeded!" : res; 
	}


	private static String executeHTTP(final String method, final  String endpoint, final Map<String, String> params) throws InterruptedException, ExecutionException{ 
		return  new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... prms) {

				if(method.contentEquals(POST))
					return post(endpoint, params);
				return null;

			}

			@Override
			protected void onPostExecute(String msg) {
			}
		}.execute(null, null, null).get();
	}

	// see http://androidsnippets.com/executing-a-http-post-request-with-httpclient
	private static String post(String endpoint, Map<String, String> params) {
		// Create a new HttpClient and Post Header
		String res = "succeed";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(endpoint);
		Log.v("REQUEST", httppost.getRequestLine().toString());
		Log.v("ENDPOINT", endpoint);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// Add your data
			for (Map.Entry<String, String> entry : params.entrySet()){
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			for(NameValuePair nvp : nameValuePairs)
				Log.v("POST BODY", nvp.getName() + ": " +  nvp.getValue());

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			

			int responseCode = response.getStatusLine().getStatusCode();
			Log.i("NOAM","Status Code : " + responseCode );
			switch(responseCode)
			{
			case 200:
				HttpEntity entity = response.getEntity();
				if(entity != null)
				{
					String responseBody = EntityUtils.toString(entity);
					Log.v("NOAM","Response body: " + responseBody);
				}
				break;
			} 

		} catch (ClientProtocolException e) {
			Log.i("NOAM", "Client Protocol exception: " + e.toString());
			res = "Client Protocol exception: " + e.toString();
		} catch (IOException e) {
			Log.i("NOAM", "IOException: " + e.toString());
			res = "IOException: " + e.toString();
		}catch (Exception e) {
			res = "Exception: " + e.toString();
			e.printStackTrace();
		}
		return res;
	} 

}

