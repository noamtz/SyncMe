package coupling.app.BL;

import org.json.JSONObject;

import coupling.app.Ids;
import coupling.app.Main;
import coupling.app.Utils;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.ui.FragmentShopList;
import coupling.app.ui.ShopList;

public abstract class AppFeature {

	protected CategoryType categoryType;
	
	public void recieveData(JSONObject data, ActionType actionType){
		switch (categoryType) {
		case SHOPLIST_OVERVIEW:
			Utils.sendNotification("New Shop List Recieved", Main.class,data);
			break;

		case SHOPLIST:
			Utils.sendNotification("New Shop List Item Recieved", ShopList.class, data);
			break;
		case CALENDER:
			Utils.LogError("AppFeature", "not implemented CALENDER case");
			break;
		default:
			break;
		}
	}
	
	public abstract boolean updateId(Ids ids);
		
}
