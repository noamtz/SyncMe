package coupling.app;

import static coupling.app.data.Constants.LOCALID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.nit.coupling.R;

import coupling.app.BL.BLFactory;
import coupling.app.com.AppFeature;
import coupling.app.data.DAL;
import coupling.app.data.Enums;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
public class Mediator {
	
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
				if(json.has("Details")){
					Utils.Log("Mediator", "manage", json.getJSONObject("Details").toString());
					updateLocalEntry(json.getJSONObject("Details"));
				} else if(json.has("error")){
					Utils.showToast(json.getString("error"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateLocalEntry(JSONObject data){
		try {

			if(data.getInt("Action") == ActionType.CREATE.value()){
				Long dbId = data.getLong(LOCALID);
				AppFeature feature = getAppFeature(data.getInt("Type"), dbId);
				if(dbId != null && feature != null){
					feature.updateId(new Ids(dbId, data.getLong("MessageId")));
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public AppFeature getAppFeature(int category, Long LocalId){
		if(category == CategoryType.SHOPLIST.value()){
			Utils.Log("Mediator", "getAppFeature", "listid : " + LocalId);
			return BLFactory.getInstance().getShopList(LocalId);
		}
		else if(category == CategoryType.SHOPLIST_OVERVIEW.value())
			return BLFactory.getInstance().getShopListOverview();
		return null;
	}

	public void deliverMessage(JSONObject message){
		try {
			String m = message.getString("message");
			message = new JSONObject(m);
			JSONObject data = new JSONObject(message.getString("data"));

			Long LocalId = null;
			if(data.has("GlobalListId")){
				LocalId = DAL.getInstance().getLocalId(CategoryType.SHOPLIST_OVERVIEW, data.getLong("GlobalListId"));
			}

			AppFeature feature = getAppFeature(message.getInt("type"), LocalId);
			if(feature != null){
				ActionType action = Enums.toActionType(message.getInt("action"));
				feature.recieveData(data, action);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
