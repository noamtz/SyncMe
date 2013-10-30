package syncme.app.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {

	private int id;
	private String type;
	private Map<String, Object> data ;
	
	public Message(){
		this.id = -1;
		this.data = new HashMap<String, Object>();
	}
	
	public Message(String type,  Map<String, Object> data){
		this.type = type;
		this.data = data;
		this.id = -1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public JSONObject toJson(){
		JSONObject message = null;
		try{
			message = new JSONObject();
			if(id != -1)
				message.put("id", id);
			message.put("type", type);
			message.put("data", new JSONObject(data));
		}catch(JSONException e){
			e.printStackTrace();
		}
		return message;
	}
	
}
