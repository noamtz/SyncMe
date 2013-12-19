package coupling.app.com;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import coupling.app.App;
import coupling.app.Utils;

import static coupling.app.com.Constants.*;

public class API { // Mabe move the ITask responsibility to another object

	private String TAG = this.getClass().getName(); 

	private User sender;
	private ArrayList<ITask> taskers;
	
	private static API api;
	
	private API(){
		this.sender = App.getOwner();
		taskers = new ArrayList<ITask>();
	}
	
	public static API getInstance(){
		if(api == null)
			api = new API();
		return api;
	}
	
	public void registerUser(){
		Request request = new Request();
		
		String userDetails = sender.toJson().toString();

		request.setMethod(REGISTER);
		request.setParams(userDetails);
		
		ServerUtils.post(request, taskers);
	}

	public void sync(Message message){
		Request request = new Request();
		
		try{

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(MESSAGE, message.toJson().toString());
			
			request.setMethod(SYNC);
			request.setParams(params.toString());
			
			ServerUtils.post(request, taskers);
			
		}catch(JSONException e){
			e.printStackTrace();
		}	

	}

	public void invite(User reciever){
		Request request = new Request();

		try{

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(FRIEND_EMAIL, reciever.getEmail());

			request.setMethod(INVITE);
			request.setParams(params.toString());

			ServerUtils.post(request, taskers);
		}catch(JSONException e){
			e.printStackTrace();
		}	
	}

	
	public void registerTasker(ITask tasker){
		if(taskers.add(tasker))
			Utils.Log(TAG, "registerTasker", tasker.getClass().getName() + " is register successfully");
	}
	
	public void unRegisterTasker(ITask tasker){
		if(taskers.remove(tasker))
			Utils.Log(TAG, "unRegisterTasker", tasker.getClass().getName() + " unregister successfully");
	}
}
