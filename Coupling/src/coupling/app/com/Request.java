package coupling.app.com;


import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import coupling.app.data.Enums.HttpType;
import static coupling.app.com.Constants.*;

public class Request {

	private UUID id;
	private String method;
	private JSONObject params;
	
	private String serverIP;
	private HttpType httpType;
	

	public Request(){
		this.id = null;
	}
	
	public Request(String method, JSONObject params, String serverIP){
		this.method = method;
		this.params = params;
		this.serverIP = serverIP;
	}

	public HttpType getHttpType() {
		return httpType;
	}
	
	public void setHttpType(HttpType httpType) {
		this.httpType = httpType; 
	}

	
	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	public String prepareToPost(){
		JSONObject json = new JSONObject();
		try {
			json.put(METHOD, method);
			json.put(PARAMS, params);
			return json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
