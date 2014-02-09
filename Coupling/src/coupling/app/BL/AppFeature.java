package coupling.app.BL;

import org.json.JSONObject;

import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.ui.FragmentShopList;

public abstract class AppFeature {

	protected CategoryType categoryType;
	
	public void recieveData(JSONObject data, ActionType actionType){
		switch (categoryType) {
		case SHOPLIST_OVERVIEW:
			Utils.sendNotification("New Shop List Recieved", FragmentShopList.class,data);
			break;

		case SHOPLIST:
			Utils.sendNotification("New Shop List Item Recieved", FragmentShopList.class, data);
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
