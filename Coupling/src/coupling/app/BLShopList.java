package coupling.app;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import coupling.app.com.API;
import coupling.app.com.AppFeature;
import coupling.app.com.ITask;
import coupling.app.com.Message;
import coupling.app.data.DALShopList;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;

public class BLShopList extends AppFeature {

	private static final String ID = "UId";
	private static final String ITEM_NAME = "ItemName";
	private static final String QUANTITY = "Quantity";
	private static final String IS_DONE = "IsDone";

	private DALShopList dataSource;
	private API api;
	private ITask tasker;



	public BLShopList(long listId){
		dataSource = new DALShopList(listId);
		api = API.getInstance();
		categoryType = CategoryType.SHOPLIST;
	}

	public boolean createItem(String name, int quantity){
		return createItem(UUID.randomUUID().toString(),name, quantity, true);
	}

	public boolean createItem(String UId, String name, int quantity, boolean remote){
		boolean res = dataSource.createItem(UId, name, quantity);

		if(remote) {
			Message message = new Message();
			message.getData().put(ID, UId);
			message.getData().put(ITEM_NAME, name);
			message.getData().put(QUANTITY, quantity);
			
			message.setCategoryType(categoryType);
			message.setActionType(ActionType.CREATE);

			api.sync(message);
		}
		return res;
	}
	public boolean updateItem(Ids ids, String name, Integer quantity, Boolean isDone){
		return updateItem(ids, name, quantity, isDone, true);
	}
	public boolean updateItem(Ids ids, String name, Integer quantity, Boolean isDone, boolean remote){
		boolean res = dataSource.updateItem(ids, name, quantity, isDone);
		if(remote){
			Message message = new Message();
			message.getData().put(ID, ids.getGlobalId());
			if(name != null)
				message.getData().put(ITEM_NAME, name);
			if(quantity != null)
				message.getData().put(QUANTITY, quantity);
			if(isDone != null)
				message.getData().put(IS_DONE, isDone);
			
			message.setCategoryType(categoryType);
			message.setActionType(ActionType.UPDATE);
			api.sync(message);
		}
		return res;
	}

	public boolean deleteItem(Ids ids){
		return deleteItem(ids, true);
	}

	public boolean deleteItem(Ids ids, boolean remote){
		boolean res = dataSource.deleteItem(ids);
		if(remote){
			Message message = new Message();
			message.getData().put(ID, ids.getGlobalId());
			
			message.setCategoryType(categoryType);
			message.setActionType(ActionType.DELETE);

			api.sync(message);
		}
		return res;
	}

	public Cursor getSource(){
		return dataSource.getSource();
	}

	@Override
	public void recieveData(JSONObject data, ActionType actionType) {
		try{
			String UId = data.getString(ID);
			Ids ids = new Ids();
			switch (actionType) {
			case CREATE:
				createItem(UId, data.getString(ITEM_NAME), data.getInt(QUANTITY), false);
				break;

			case UPDATE:
				ids.setGlobalId(UId);
				updateItem(ids, data.getString(ITEM_NAME), data.getInt(QUANTITY), data.getBoolean(IS_DONE), false);
				break;
			case DELETE:
				ids.setGlobalId(UId);
				deleteItem(ids, false);
				break;
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
}
