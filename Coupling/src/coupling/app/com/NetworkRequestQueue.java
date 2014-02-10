package coupling.app.com;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import coupling.app.App;
import coupling.app.Mediator;
import coupling.app.Utils;

public class NetworkRequestQueue {

	private static NetworkRequestQueue requestQueue;

	public static String SERVER_URL = "http://coupling.herobo.com/api/syncMeApp.php";

	private NetworkRequestQueue(){}

	public static NetworkRequestQueue getInstance(){
		if(requestQueue == null)
			requestQueue = new NetworkRequestQueue();
		return requestQueue;
	}

	/**
	 * Log or request TAG
	 */
	public static final String TAG = "VolleyPatterns";
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;

	//* VOLLEY *//
	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(App.getAppCtx());
		}

		return mRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified
	 * then it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(com.android.volley.Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(com.android.volley.Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important
	 * to specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public void postJson(JSONObject json){
		Utils.Log(TAG, "postJson", json.toString());
		JsonObjectRequest req = new JsonObjectRequest(SERVER_URL, json, responseHandler(), errorHandler());
		addToRequestQueue(req);
	}

	public JSONObject postFutureJson(JSONObject json){
		RequestFuture<JSONObject> future = RequestFuture.newFuture();
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, SERVER_URL, json, future, future);
		addToRequestQueue(request);
		JSONObject response = null;
		try {
			response = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return response;

	}

	private Response.Listener<JSONObject> responseHandler(){

		return  new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Utils.Log(TAG,"responseHandler", response.toString());
				Mediator.getInstance().manage(response);
			}
		};
	}

	private Response.ErrorListener errorHandler(){
		return  new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Error: ", error.getMessage());
				Utils.showToast("volley network:onErrorResponse= " + error.getMessage());
			}
		};
	}
}
