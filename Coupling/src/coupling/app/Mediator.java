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
import coupling.app.models.Ids;
/**
 * This module function as a "cross roads" every network http response
 * is flow throw here for redirect to appropriate BL
 * @author Noam Tzumie
 *
 */
public class Mediator {

	private static final String TAG = "Mediator";
	private static Mediator mediator;


	private Mediator(){
	}

	public synchronized static Mediator getInstance(){
		if(mediator == null)
			mediator = new Mediator();
		return mediator;
	}

	public void manage(JSONObject json){
		manage(json,false);
	}
	
	/**
	 * Getting the incoming json and determine 
	 * the appropriate action
	 * @param json
	 * @param background
	 */
	public synchronized void manage(JSONObject json , boolean background){
		try {
			if(json != null){
				if(json.has("message")){ // After getMessage api method to recive message from partner
					deliverMessage(json);
				} else if(json.has("Details")){ // After sync api method to retrieve globalID
					updateGlobalId(json.getJSONObject("Details"));
				} else if(json.has("error")){ // When error occur
					Utils.Log(TAG, "manage",json.toString());
					if(!background)
						Utils.showToast(json.getString("error"));
				} else if(json.has("statusMessage")){ // for optional information (invite , register etc..)
					Utils.Log(TAG, "manage",json.toString());
					if(!background)
						Utils.showToast(json.getString("statusMessage"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the appropriate AppFeature for specific newly created item 
	 * with server global ID
	 * @param data
	 */
	public synchronized void updateGlobalId(JSONObject data){
		try {
			
			if(data.getInt("Action") == ActionType.CREATE.value()) {
				//Update global db by getting the local AppFeature item localId (dbId)
				//from the json data
				Long DBId = null;
				if(data.has(LOCALID))
					DBId = data.getLong(LOCALID);
				//For ShopList items
				Long localListId = null;
				if(data.has(LOCAL_LIST_ID))
					localListId = data.getLong(LOCAL_LIST_ID);
				
				AppFeature feature = getAppFeature(data.getInt("Type"), localListId);

				if(DBId != null && feature != null) {
					feature.updateId(new Ids(DBId, data.getLong("MessageId")));
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get the approariage AppFeature by categoryCode
	 * @param categoryCode
	 * @param localListId
	 * @return
	 */
	public synchronized AppFeature getAppFeature(int categoryCode, Long localListId){
		CategoryType category = Enums.toCategoryType(categoryCode);
		switch (category) {
		case SHOPLIST: return BLFactory.getInstance().getShopList(localListId);
		case SHOPLIST_OVERVIEW: return BLFactory.getInstance().getShopListOverview();
		case CALENDER: return BLFactory.getInstance().getCalendarEvents();
		case NOT_DEFINED:
			Utils.LogError(TAG, "getAppFeature" ,"Category type is not defined");
		}
		return null;
	}
	/**
	 * Deliver incoming item to appropriate AppFeature 
	 * @param message
	 */
	public synchronized void deliverMessage(JSONObject message){
		try {

			message = new JSONObject(message.getString("message"));
			JSONObject data = new JSONObject(message.getString("data"));

			ActionType action = Enums.toActionType(message.getInt("action"));

			//For ShopList
			Long LocalId = null;
			if(data.has("GlobalListId")){
				LocalId = DAL.getInstance().getLocalId(CategoryType.SHOPLIST_OVERVIEW, data.getLong("GlobalListId"));
			}

			AppFeature feature = getAppFeature(message.getInt("type"), LocalId);

			if(feature != null) {
				feature.recieveData(data, action);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
