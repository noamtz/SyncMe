package coupling.app.BL;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.com.API;
import coupling.app.com.IBLConnector;
import coupling.app.com.Message;
import coupling.app.data.DALShopListOverview;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import static coupling.app.data.Constants.*;

public class BLShopListOverview extends AppFeature {

	private static final String TITLE = "Title";
	private DALShopListOverview dataSource;
	private API api;

	private IBLConnector connector;

	public BLShopListOverview(){
		dataSource = DALShopListOverview.getInstance();
		categoryType = CategoryType.SHOPLIST_OVERVIEW;
		api = API.getInstance();
	}

	public Cursor getSource(){
		return dataSource.getSource();
	}

	public boolean createList(String title){
		return createList(null,title,null,null, true);
	}

	public boolean createList(Long UId, String title,Boolean isMine,Boolean isLocked, boolean remote){
		long localId = dataSource.createList(UId,title,isMine);
		boolean isCreated = (localId != -1); 

		if(remote && isCreated){
			Message message = new Message();

			message.getData().put(LOCALID, localId);
			message.getData().put(UID, UId);
			message.getData().put(TITLE, title);

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.CREATE);

			api.sync(message);
		}
		return isCreated;
	}

	public boolean deleteItem(Ids ids){
		return deleteItem(ids, true);
	}

	public boolean deleteItem(Ids ids, boolean remote){
		boolean res = dataSource.deleteList(ids);
		if(remote && res){
			Message message = new Message();

			message.getData().put(UID, ids.getGlobalId());

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.DELETE);

			api.sync(message);
		}
		return res;
	}

	@Override
	public void recieveData(JSONObject data, ActionType actionType) {

		try{	
			Ids ids = new Ids();
			if(data.has(UID) && !data.get(UID).equals(null))
				ids.setGlobalId(data.getLong(UID));

			String title = null;

			if(data.has(TITLE)) title = data.getString(TITLE);

			switch (actionType) {
			case CREATE:
				createList(ids.getGlobalId(), title, false, false, true);
				break;
			case DELETE:
				deleteItem(ids, false);
				break;
			case UPDATE:
				Utils.LogError("BLShopListOverview", "not implemented UPDATE case");
				break;
			}
			if(connector == null)
				super.recieveData(data, actionType);
		}catch(JSONException e){
			e.printStackTrace();
		}

		if(connector != null)
			connector.Refresh();
	}

	@Override
	public boolean updateId(Ids ids) {
		if(dataSource.updateId(ids) && connector != null){
			connector.Refresh();
			return true;
		} else {
			return false;
		}
	}

	public void setBLConnector(IBLConnector connector){
		this.connector = connector;
	}
	public void unsetBLConnector(){
		this.connector = null;
	}


}
