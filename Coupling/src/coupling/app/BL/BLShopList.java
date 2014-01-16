package coupling.app.BL;


import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.com.API;
import coupling.app.com.AppFeature;
import coupling.app.com.IBLConnector;
import coupling.app.com.Message;
import coupling.app.data.DALShopList;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.ShopListItem;
import static coupling.app.data.Constants.*;

public class BLShopList extends AppFeature{

	private static final String TAG = "BLShopList";
	private DALShopList dataSource;
	private API api;
	private Long GlobalListId;
	private Long listId;

	IBLConnector connector;

	public BLShopList(){
	}

	public BLShopList(long listId){
		dataSource = new DALShopList(listId);
		api = API.getInstance();
		categoryType = CategoryType.SHOPLIST;
		GlobalListId = dataSource.getGlobalListId();
		this.listId = listId;
	}

	public void setBLConnector(IBLConnector connector){
		this.connector = connector;
	}
	public void unsetBLConnector(){
		this.connector = null;
	}


	public boolean createItem(String name, int quantity){
		return createItem(new ShopListItem(listId, name, quantity) ,  true);
	}

	public boolean createItem(ShopListItem item, boolean remote){
		item.getIds().setDBId(dataSource.createItem(item));
		boolean isCreated = (item.getIds().getDBId() != -1);

		if(remote && isCreated) {
			Message message = new Message();

			GlobalListId = (GlobalListId == null) ? dataSource.getGlobalListId() : GlobalListId;

			message.setData(item.toNetwork());
			message.getData().put(GLOBAL_LIST_ID, GlobalListId);
			
			message.setCategoryType(categoryType);
			message.setActionType(ActionType.CREATE);

			api.sync(message);
		}
		//LOGS
		if(!isCreated)
			Utils.LogError(TAG,"createItem" , "Failed to create: " + item);
		//LOGS

		return isCreated;
	}
	public boolean updateItem(ShopListItem item){
		return updateItem(item, true);
	}
	public boolean updateItem(ShopListItem item, boolean remote){
		boolean isUpdated = dataSource.updateItem(item);
		if(remote && isUpdated){
			Message message = new Message();

			GlobalListId = (GlobalListId == null) ? dataSource.getGlobalListId() : GlobalListId;
			
			message.setData(item.toNetwork());
			message.getData().put(GLOBAL_LIST_ID, GlobalListId);
			
			message.setCategoryType(categoryType);
			message.setActionType(ActionType.UPDATE);
			api.sync(message);
		}
		//LOGS
		if(!isUpdated)
			Utils.LogError(TAG,"updateItem" , "Failed to update: " + item);
		//LOGS
		return isUpdated;
	}

	public boolean deleteItem(Ids ids){
		return deleteItem(ids, true);
	}

	public boolean deleteItem(Ids ids, boolean remote){
		boolean isDeleted = dataSource.deleteItem(ids);
		if(remote && isDeleted){
			Message message = new Message();

			GlobalListId = (GlobalListId == null) ? dataSource.getGlobalListId() : GlobalListId;

			message.getData().put(GLOBAL_LIST_ID, GlobalListId);
			message.getData().put(UID, ids.getGlobalId());

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.DELETE);

			api.sync(message);
		}
		//LOGS
		if(!isDeleted)
			Utils.LogError(TAG,"deleteItem" , "Failed to delete: " + ids);
		//LOGS
		return isDeleted;
	}

	public boolean updateId(Ids ids){
		return dataSource.updateId(ids);
	}

	public Cursor getSource(){
		return dataSource.getSource();
	}

	public Long getShopListId(){
		return listId;
	}



	@Override
	public void recieveData(JSONObject data, ActionType actionType) {
		try{

			ShopListItem item = new ShopListItem(listId);
			if(data.has(UID) && !data.get(UID).equals(null))
				item.getIds().setGlobalId(data.getLong(UID));


			if(data.has(ITEM_NAME)) item.setName(data.getString(ITEM_NAME));
			if(data.has(ITEM_QUANTITY)) item.setQuantity(data.getInt(ITEM_QUANTITY));
			if(data.has(IS_DONE)) item.setIsDone(data.getBoolean(IS_DONE));

			switch (actionType) {
			case CREATE:
				item.setIsMine(false);
				createItem(item, false);
				break;

			case UPDATE:	
				updateItem(item, false);
				break;
			case DELETE:
				deleteItem(item.getIds(), false);
				break;
			}

			if(connector != null)
				connector.Refresh();
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
}
