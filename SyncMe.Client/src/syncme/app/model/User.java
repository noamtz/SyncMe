package syncme.app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private int id;
	private String firstname;
	private String lastname;
	private String email;
	private String regId;
	
	
	public User() { 
		//initialize id, regid
		this.id = -1;
		this.regId = "";
	}
	 
	public User(String firstname , String lastname, String email) { 
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		//initialize id
		this.id = -1;
		this.regId = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegid() {
		return regId;
	}

	public void setRegid(String regid) {
		this.regId = regid;
	}
	
	public JSONObject toJson(){
		JSONObject user = null;
		try{
			user = new JSONObject();
			if(id != -1)
				user.put("id", id);
			user.put("firstname", firstname);
			user.put("lastname", lastname);
			user.put("email", email);
			if(!regId.contentEquals(""))
				user.put("regId", regId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		return user;
	}
}
