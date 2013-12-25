package coupling.app.com;

import org.json.JSONObject;

import coupling.app.Ids;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;

public abstract class AppFeature {

	protected CategoryType categoryType;
	
	public abstract void recieveData(JSONObject data, ActionType actionType);
}
