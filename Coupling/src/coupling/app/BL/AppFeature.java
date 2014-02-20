package coupling.app.BL;

import org.json.JSONObject;

import coupling.app.Main;
import coupling.app.Utils;
import coupling.app.com.API;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.Ids;
import coupling.app.ui.FragmentShopList;
import coupling.app.ui.ShopList;

/**
 * This class abstract the BL layer
 * BLs are interacting with the database module
 * and the network module
 * @author Noam Tzumie
 *
 */
public abstract class AppFeature {

	protected CategoryType categoryType;
	protected API api;
	
	/**
	 * Called whenever new item comes from GCM
	 * and send notification
	 * @param data
	 * @param actionType
	 */
	public void recieveData(JSONObject data, ActionType actionType){
		
		switch (categoryType) {
		case SHOPLIST_OVERVIEW:
			Utils.sendNotification("Recieved new list ", Main.class,data);
			break;

		case SHOPLIST: 
			Utils.sendNotification("Recieved new list item", ShopList.class, data);
			break;
		case CALENDER:
			Utils.sendNotification("Recieved new calender event", Main.class, data);
			break;
		default:
			break;
		}
	}
	/**
	 * Update the global Id of an item after created locally
	 * @param ids
	 * @return
	 */
	public abstract boolean updateId(Ids ids);
		
}
