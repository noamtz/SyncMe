package coupling.app.models;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkOfflineItem {
	private Long id;
	private JSONObject json;
	
	public NetworkOfflineItem(Long id, String json){
		this.id = id;
		try {
			this.json = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	@Override
	public boolean equals(Object o) {
		NetworkOfflineItem other = (NetworkOfflineItem)o;
		return this.id == other.id;
	}
	
	
}
