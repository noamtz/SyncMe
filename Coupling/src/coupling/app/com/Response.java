package coupling.app.com;

import org.json.JSONException;
import org.json.JSONObject;

import coupling.app.Utils;

public class Response {
	
	private String response;
	private Long messageId;
	
	public Response(){
	}
	
	public Response(JSONObject response){
		this.response = response.toString();
		try {
			messageId = response.getJSONObject("message").getLong("id");
		} catch (JSONException e) {
			Utils.Log(Response.class.getName(), "C-tor", "No Message Id");
		}
	}
	
	public Response(String response){
		this.response = response;
	}
	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	
}
