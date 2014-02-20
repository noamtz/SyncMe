package coupling.app.BL;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import coupling.app.Utils;
import coupling.app.com.API;
import coupling.app.com.IBLConnector;
import coupling.app.com.Message;
import coupling.app.data.DALShopListOverview;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.Ids;
import coupling.app.models.ShopListOverView;
import static coupling.app.data.Constants.*;

/**
 * This class take care for manageing lists (for more methods details look at BLShopList)
 * @author Noam Tzumie
 *
 */
public class BLShopListOverview extends AppFeature {

	private DALShopListOverview dataSource;

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
		ShopListOverView list = new ShopListOverView();
		list.setTitle(title);
		return createList(list, true);
	}

	public boolean createList(ShopListOverView list, boolean remote){
		long localId = dataSource.createList(list);
		boolean isCreated = (localId != -1); 

		if(remote && isCreated){
			Message message = new Message();

			message.setData(list.toNetwork());
			message.getData().put(LOCALID, localId);
			message.getData().put(UID, list.getGlobalId());

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
			message.getData().put(LOCALID, ids.getDBId());

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.DELETE);

			api.sync(message);
		}
		return res;
	}

	@Override
	public synchronized void recieveData(JSONObject data, ActionType actionType) {

		try{	
			ShopListOverView list = new ShopListOverView();
			if(data.has(UID) && !data.get(UID).equals(null))
				list.getIds().setGlobalId(data.getLong(UID));


			if(data.has(TITLE)) list.setTitle(data.getString(TITLE));
			if(data.has(TOTAL_ITEMS)) list.setTotalItems(data.getInt(TOTAL_ITEMS));

			//Unlock item
			list.setIsLocked(false);
			
			switch (actionType) {
			case CREATE:
				list.setIsMine(false);
				createList(list, false);
				break;
			case DELETE:
				if(dataSource.isItemExist(list.getIds().getGlobalId())){
					deleteItem(list.getIds(), false);
				}
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
