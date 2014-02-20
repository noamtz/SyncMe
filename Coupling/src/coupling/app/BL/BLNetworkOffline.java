package coupling.app.BL;

import static coupling.app.data.Constants.*;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import coupling.app.Utils;

import coupling.app.data.DALNetworkQueue;
import coupling.app.data.Enums;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.NetworkOfflineItem;

public class BLNetworkOffline {

	private static ArrayList<NetworkOfflineItem> dbQueue;

	private static BLNetworkOffline blNetworkOffline;
	private static DALNetworkQueue dalNetworkQueue = DALNetworkQueue.getInstance();

	private BLNetworkOffline(){
		dbQueue = new ArrayList<NetworkOfflineItem>();
	}

	public static BLNetworkOffline getInstance(){
		if(blNetworkOffline == null)
			blNetworkOffline = new BLNetworkOffline();
		return blNetworkOffline;
	}

	public ArrayList<NetworkOfflineItem> getQueue(){
		ArrayList<NetworkOfflineItem> dalQueue = dalNetworkQueue.getQueue();
		for(NetworkOfflineItem item : dalQueue){
			if(!dbQueue.contains(item)){
				dbQueue.add(item);
			}
		}
		return dbQueue;
	}

	public void handleNetworkItem(NetworkOfflineItem item){
		try {
			JSONObject message = item.getJson().getJSONObject("params").getJSONObject("message");
			CategoryType cat = Enums.toCategoryType(message.getInt("type"));
			ActionType action = Enums.toActionType(message.getInt("action"));
			JSONObject data = message.getJSONObject("data");

			if(action == ActionType.UPDATE || action == ActionType.DELETE){

				if(data.has(UID) && data.get(UID).equals(null)){
					if(action == ActionType.UPDATE)
						message.put("action",ActionType.CREATE.value());
					else
						data.put(UID, retrieveGlobalId(data.getLong(LOCALID), cat));
				}
				if(data.has(GLOBAL_LIST_ID) && data.getLong(GLOBAL_LIST_ID) == 0){
					data.put(GLOBAL_LIST_ID, retrieveGlobalListId(data.getLong(LOCAL_LIST_ID)));
				}
				message.put("data", data);
				item.getJson().getJSONObject("params").put("message",message);
			} else if(action == ActionType.CREATE && cat == CategoryType.SHOPLIST){
				if(data.has(GLOBAL_LIST_ID) && data.getLong(GLOBAL_LIST_ID) == 0){
					data.put(GLOBAL_LIST_ID, retrieveGlobalListId(data.getLong(LOCAL_LIST_ID)));
				}
				message.put("data", data);
				item.getJson().getJSONObject("params").put("message",message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Long retrieveGlobalId(Long localId, CategoryType cat){
		Long res = new Long(0);
		switch (cat) {
		case SHOPLIST_OVERVIEW: return dalNetworkQueue.retrieveGlobalId(localId, "ShopListOverview");
		case SHOPLIST: return dalNetworkQueue.retrieveGlobalId(localId, "ShopList");
		case CALENDER: return dalNetworkQueue.retrieveGlobalId(localId, "CalendarEvents");
		}
		if(res == 0) Log.e("BLNetworkOffline",  "retrieveGlobalId: failed to retrieve for: " +cat );
		return res;
	}

	public Long retrieveGlobalListId(Long localListId){
		Long res = dalNetworkQueue.retrieveGlobalListId(localListId);
		if(res == 0) Log.e("BLNetworkOffline", "retrieveGlobalListId: failed to retrieve for: " +localListId );
		return res;
	}

	public void remove(NetworkOfflineItem item){
		if(dalNetworkQueue.remove(item.getId()))
			dbQueue.remove(item);
	}

	public void add(JSONObject json){
		JSONObject message;
		try {
			message = json.getJSONObject("params").getJSONObject("message");
			CategoryType cat = Enums.toCategoryType(message.getInt("type"));
			ActionType action = Enums.toActionType(message.getInt("action"));
			Long dbId = message.getJSONObject("data").getLong(LOCALID);
			Object UId = message.getJSONObject("data").get(UID);
			long itemId = -1; 
			if((itemId = dalNetworkQueue.isAppItemExist(cat, dbId)) != -1){
				dalNetworkQueue.remove(itemId);
			}
			boolean notEnter = (action == ActionType.DELETE) && UId.equals(null);
			Utils.Log("BLNetworkOffline", "Not enter: " + notEnter + " UID: "+ UId.equals(null)+ " acrion: " + (action == ActionType.DELETE));
			if(!notEnter)
				dalNetworkQueue.add(json, cat, dbId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
