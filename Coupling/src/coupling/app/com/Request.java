package coupling.app.com;


import java.util.UUID;

import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.data.Enums.HttpType;

public class Request {

	private UUID id;
	private String method;
	private String params;
	
	private String serverIP;
	private HttpType httpType;
	

	public Request(){
		this.id = null;
	}
	
	public Request(String method, String params, String serverIP){
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
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	
	
}
