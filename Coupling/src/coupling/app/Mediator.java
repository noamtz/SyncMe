package coupling.app;

import static coupling.app.data.Constants.*;

import org.json.JSONException;
import org.json.JSONObject;

import coupling.app.BL.AppFeature;
import coupling.app.BL.BLFactory;
import coupling.app.data.DAL;
import coupling.app.data.Enums;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
public class Mediator {

	private static final String TAG = "Mediator";
	private static Mediator mediator;


	private Mediator(){
	}

	public static Mediator getInstance(){
		if(mediator == null)
			mediator = new Mediator();
		return mediator;
	}

	public void manage(JSONObject json){
		try {
			if(json != null){
				if(json.has("message")){
					deliverMessage(json);
				} else if(json.has("Details")){
					updateGlobalId(json.getJSONObject("Details"));
				} else if(json.has("error")){
					Utils.showToast(json.getString("error"));
				} 
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateGlobalId(JSONObject data){
		try {

			if(data.getInt("Action") == ActionType.CREATE.value()){
				Long localListId = null;
				Long DBId = null;
				if(data.has(LOCAL_LIST_ID))
					localListId = data.getLong(LOCAL_LIST_ID);
				if(data.has(LOCALID))
					DBId = data.getLong(LOCALID);
				
				AppFeature feature = getAppFeature(data.getInt("Type"), localListId);
				
				if(DBId != null && feature != null){
					feature.updateId(new Ids(DBId, data.getLong("MessageId")));
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public AppFeature getAppFeature(int categoryCode, Long localListId){
		CategoryType category = Enums.toCategoryType(categoryCode);
		switch (category) {
		case SHOPLIST: return BLFactory.getInstance().getShopList(localListId);
		case SHOPLIST_OVERVIEW: return BLFactory.getInstance().getShopListOverview();
		case CALENDER: return BLFactory.getInstance().getCalendarEvents(localListId);
		case NOT_DEFINED:
			Utils.LogError(TAG, "getAppFeature" ,"Category type is not defined");
		}
		return null;
	}

	public void deliverMessage(JSONObject message){
		try {
			message = new JSONObject(message.getString("message"));
			JSONObject data = new JSONObject(message.getString("data"));

			Long LocalId = null;
			if(data.has("GlobalListId")){
				LocalId = DAL.getInstance().getLocalId(CategoryType.SHOPLIST_OVERVIEW, data.getLong("GlobalListId"));
			}
			Utils.Log("MEDIATOR", "LOCALID: " + LocalId);
			AppFeature feature = getAppFeature(message.getInt("type"), LocalId);
			if(feature != null) {
				ActionType action = Enums.toActionType(message.getInt("action"));
				feature.recieveData(data, action);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
