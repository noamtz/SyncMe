package coupling.app.com;

import org.json.JSONObject;

import coupling.app.Utils;

public class Response {
	
	private String response;
	private Long messageId;
	private JSONObject json;
	
	public Response(){
	}
	
	public Response(JSONObject response){
		this.json = response;
		this.response = response != null ? response.toString() : null;
		try {
			messageId = response.getJSONObject("message").getLong("id");
		} catch (Exception e) {
			Utils.Log(Response.class.getName(), "C-tor", "No Message Id");
		}
	}
	
	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
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
