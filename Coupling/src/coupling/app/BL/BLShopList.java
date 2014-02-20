package coupling.app.BL;


import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import coupling.app.Utils;
import coupling.app.com.API;
import coupling.app.com.IBLConnector;
import coupling.app.com.Message;
import coupling.app.data.DALShopList;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.Ids;
import coupling.app.models.ShopListItem;
import static coupling.app.data.Constants.*;

/**
 * This class take care for interacting with specific shopList
 * @author Noam Tzumie
 *
 */
public class BLShopList extends AppFeature{

	private static final String TAG = "BLShopList";
	private DALShopList dataSource;

	private Long GlobalListId;
	private Long listId;

	IBLConnector connector;

	/**
	 * Init the global fields 
	 * 
	 * @param _listId
	 */
	public BLShopList(long _listId){
		api = API.getInstance();
		dataSource = new DALShopList(_listId);
		GlobalListId = dataSource.getGlobalListId();
		listId = _listId;
		categoryType = CategoryType.SHOPLIST;
	}

	public void setBLConnector(IBLConnector connector){
		Utils.Log(TAG, "setBLConnector", "ListId: " +listId+ " ,Connector Id: " + connector + " BLShopList Id: " + this);
		this.connector = connector;
	}
	
	public void unsetBLConnector(){
		Utils.Log(TAG, "unsetBLConnector", "ListId: " +listId+ " ,Connector Id: " + connector + " BLShopList Id: " + this);
		this.connector = null;
	}


	public boolean createItem(String name, int quantity){
		return createItem(new ShopListItem(listId, name, quantity) ,  true);
	}
	/**
	 * Create new shopList item
	 * @param item - the created item
	 * @param remote - determine if to send to the network
	 * @return
	 */
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
	/**
	 * Update shopListItem 
	 * @param item
	 * @param remote
	 * @return
	 */
	public boolean updateItem(ShopListItem item, boolean remote){
		ShopListItem updatedItem = dataSource.updateItem(item);
		if(remote && updatedItem!=null){
			Message message = new Message();

			GlobalListId = (GlobalListId == null) ? dataSource.getGlobalListId() : GlobalListId;

			message.setData(updatedItem.toNetwork());
			message.getData().put(GLOBAL_LIST_ID, GlobalListId);

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.UPDATE);
			api.sync(message);
		}
		//LOGS
		if(updatedItem==null)
			Utils.LogError(TAG,"updateItem" , "Failed to update: " + item);
		//LOGS
		return updatedItem!=null;
	}

	public boolean deleteItem(Ids ids){
		return deleteItem(ids, true);
	}

	public boolean deleteItem(Ids ids, boolean remote){
		ShopListItem deletedItem = dataSource.deleteItem(ids);
		if(remote && deletedItem!=null){
			Message message = new Message();
			
			GlobalListId = (GlobalListId == null) ? dataSource.getGlobalListId() : GlobalListId;

			message.getData().put(GLOBAL_LIST_ID, GlobalListId);
			message.getData().put(UID, ids.getGlobalId());
			message.getData().put(LOCALID, deletedItem.getLocalId());

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.DELETE);

			api.sync(message);
		}
		//LOGS
		if(deletedItem==null)
			Utils.LogError(TAG,"deleteItem" , "Failed to delete: " + ids);
		//LOGS
		return deletedItem!=null;
	}

	public boolean updateId(Ids ids){
		if(dataSource.updateId(ids) && connector != null){
			connector.Refresh();
			return true;
		} else {
			return false;
		}
	}

	public Cursor getSource(){
		return dataSource.getSource();
	}

	public Long getShopListId(){
		return listId;
	}


	/**
	 * This method recieve the newly arrived shoplist 
	 * and make the suitable action for this item (create,update,delete)
	 * and also try to refresh the shoplist listView by sending a signal to
	 * the activity
	 */
	@Override
	public synchronized void recieveData(JSONObject data, ActionType actionType) {
		
		try{
			//Insert listId to data for notification
			data.put(LOCAL_LIST_ID, listId);
			Utils.Log("BLShopList", "listId: " + listId);
			ShopListItem item = new ShopListItem(listId);
			if(data.has(UID) && !data.get(UID).equals(null))
				item.getIds().setGlobalId(data.getLong(UID));
			
			//Unlock item
			item.setIsLocked(false);


			if(data.has(ITEM_NAME)) item.setName(data.getString(ITEM_NAME));
			if(data.has(ITEM_QUANTITY)) item.setQuantity(data.getInt(ITEM_QUANTITY));
			if(data.has(IS_DONE)) item.setIsDone(data.getBoolean(IS_DONE));

			switch (actionType) {
			case CREATE:
				item.setIsMine(false);
				createItem(item, false);
				break;

			case UPDATE:
				if(dataSource.isItemExist(item.getGlobalId())){
					updateItem(item, false);
				}
				else{
					item.setIsMine(false);
					createItem(item, false);
				}
				break;
			case DELETE:
				if(dataSource.isItemExist(item.getGlobalId())){
					deleteItem(item.getIds(), false);
				}
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
}
