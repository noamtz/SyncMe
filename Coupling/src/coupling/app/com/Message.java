package coupling.app.com;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import coupling.app.Utils;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;

public class Message {

	private static final String DATA = "data";
	private static final String TYPE = "type";
	private static final String ACTION = "action";
	
	private Map<String, Object> data ;
	private CategoryType categoryType;
	private ActionType actionType;
	private Long messageServerId;
	
	public Message(JSONObject message, Long messageId){
		try {
			this.categoryType.setValue(message.getInt(TYPE));
			this.actionType.setValue(message.getInt(ACTION));
			messageServerId = messageId;
			//data = message.getJSONObject(DATA).
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Message(){
		this.data = new HashMap<String, Object>();
	}
	
	public Message(Map<String, Object> data){
		this.data = data;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public CategoryType getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(CategoryType categoryType) {
		this.categoryType = categoryType;
	}
	
	public Map<String, Object> getData() {
		return data;	
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public Long getMessageServerId() {
		return messageServerId;
	}

	public void setMessageServerId(Long messageServerId) {
		this.messageServerId = messageServerId;
	}

	public JSONObject toJson(){
		JSONObject message = null;
		try{
			message = new JSONObject();
			message.put(TYPE, categoryType.getValue());
			message.put(ACTION, actionType.getValue());
			message.put(DATA, new JSONObject(data));

		}catch(JSONException e){
			e.printStackTrace();
		}
		return message;
	}
}

