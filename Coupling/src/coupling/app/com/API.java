package coupling.app.com;

import org.json.JSONException;
import org.json.JSONObject;
import coupling.app.App;
import coupling.app.Utils;
import coupling.app.models.User;

import static coupling.app.data.Constants.*;

/**
 * This module are responsible for wrap up all the application 
 * entitys and send them to the appropriate api call to the server
 * @author Noam Tzumie
 *
 */
public class API {

	private User sender;
	private static NetworkRequestQueue networkHandler = NetworkRequestQueue.getInstance();

	private static API api;

	private API() {
		this.sender = App.getOwner();
	}


	public static API getInstance(){
		if(api == null)
			api = new API();
		return api;
	}

	public String registerUser(){
		String result = null;
		if(!Utils.isNetworkAvailable()){
			result = "No internet connection";
		} else {
			JSONObject json = networkHandler.postFutureJson(prepareJson(REGISTER,App.getOwner().toJson()));
			if(json.has("error"))
				try {
					result = json.getString("error");
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	public void sync(Message message){
		try{

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());//TODO: change back
			Utils.Log("API", "Sender id: " + sender.getEmail());
			params.put(MESSAGE, message.toJson());

			networkHandler.postJson(prepareJson(SYNC,params));

		}catch(JSONException e){
			e.printStackTrace();
		}	

	}

	public void messageRecieved(long messageId) {
		try {

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(MESSAGE_ID, messageId);

			networkHandler.postJson(prepareJson(MESSAGE_RECIEVED,params));
		} catch(JSONException e){
			e.printStackTrace();
		}	
	}

	public void getMessage(long messageId) {
		try {

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(MESSAGE_ID, messageId);

			networkHandler.postJson(prepareJson(GET_MESSAGE,params));
		} catch(JSONException e){
			e.printStackTrace();
		}	
	}

	public void invite(String email) {
		try{

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(FRIEND_EMAIL, email);

			networkHandler.postJson(prepareJson(INVITE,params));

		}catch(JSONException e){
			e.printStackTrace();
		}	
	}

	public String uninvite(String email) {
		String result = null;
		try{

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(FRIEND_EMAIL, email);

			JSONObject json = networkHandler.postFutureJson(prepareJson(INVITE,params));
			if(json.has("error"))  
				result = json.getString("error");

		}catch(JSONException e){
			e.printStackTrace();
		}	
		return result;
	}


	private JSONObject prepareJson(String method, JSONObject params){
		JSONObject json = new JSONObject();
		try {
			json.put(METHOD, method);
			json.put(PARAMS, params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
