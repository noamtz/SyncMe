package syncme.app.data;


import java.util.UUID;

public class Request {

	private UUID id;
	private String method;
	private String params;
	private String serverIP;
	
	public Request(){
		this.id = null;
	}
	
	public Request(String method, String params, String serverIP){
		this.method = method;
		this.params = params;
		this.serverIP = serverIP;
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
