package syncme.app.logic;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import syncme.app.com.ITask;
import syncme.app.com.ServerUtils;
import syncme.app.model.Message;
import syncme.app.model.Request;
import syncme.app.model.User;
import syncme.app.model.shoplist.ShopList;
import static syncme.app.logic.Constants.*;

public class BL { // Mabe move the ITask responsibility to another object

	private String TAG = this.getClass().getName(); 

	ITask tasker;
	

	public BL(ITask tasker){
		this.tasker = tasker;
	}
	
	
	public void registerUser(User user){
		Request request = new Request();
		
		String userDetails = user.toJson().toString();

		request.setMethod(REGISTER);
		request.setParams(userDetails);
		
		ServerUtils.execute(request, tasker);
	}

	public void sync(User sender, Message message){
		Request request = new Request();
		
		try{

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(MESSAGE, message.toJson().toString());
			
			request.setMethod(SYNC);
			request.setParams(params.toString());
			
			
			
			ServerUtils.execute(request, tasker);
			
		}catch(JSONException e){
			e.printStackTrace();
		}	

	}

	public void invite(User sender, User reciever){
		Request request = new Request();

		try{

			JSONObject params = new JSONObject();
			params.put(EMAIL, sender.getEmail());
			params.put(FRIEND_EMAIL, reciever.getEmail());

			request.setMethod(INVITE);
			request.setParams(params.toString());

			ServerUtils.execute(request, tasker);
		}catch(JSONException e){
			e.printStackTrace();
		}	
	}

}
