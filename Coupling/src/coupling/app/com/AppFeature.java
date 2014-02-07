package coupling.app.com;

import org.json.JSONObject;

import coupling.app.FragmentShopList;
import coupling.app.Ids;
import coupling.app.ShopList;
import coupling.app.Utils;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;

public abstract class AppFeature {

	protected CategoryType categoryType;
	
	public void recieveData(JSONObject data, ActionType actionType){
		switch (categoryType) {
		case SHOPLIST_OVERVIEW:
			Utils.sendNotification("New Shop List Recieved", FragmentShopList.class);
			break;

		case SHOPLIST:
			Utils.sendNotification("New Shop List Recieved", ShopList.class);
			break;
		}
	}
	
	public abstract boolean updateId(Ids ids);
	
	
}
