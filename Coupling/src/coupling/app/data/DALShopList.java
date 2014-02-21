package coupling.app.data;

import coupling.app.App;
import coupling.app.Utils;
import coupling.app.models.Ids;
import coupling.app.models.ShopListItem;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

/**
 * Handling all ShopList items data access crud operations
 * @author Noam Tzumie
 *
 */
public class DALShopList {

	private DBHandler dbHandler;

	private long listId;

	public DALShopList(long listId){
		dbHandler = new DBHandler(App.getAppCtx());
		this.listId = listId; 
	}

	public Cursor getSource(){
		return dbHandler.getReadableDatabase().rawQuery("SELECT * FROM ShopList WHERE ShopListId = " + listId, null);
	}

	public boolean updateId(Ids ids){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null) {
			values.put("UId", ids.getGlobalId());
			values.put(Constants.IS_LOCKED, false);
		}
		return dbHandler.getWritableDatabase().update("ShopList",values,"_id = " + ids.getDBId(), null) > 0;
	}

	public long createItem(ShopListItem item) {
		return dbHandler.getWritableDatabase().insertOrThrow("ShopList", null, item.toDb());

	}

	public ShopListItem updateItem(ShopListItem item) {
		String where = (item.getGlobalId() != null) ? "UId = " + item.getGlobalId() : "_id = " + item.getLocalId();
		boolean isUpdated = dbHandler.getWritableDatabase().update("ShopList",item.toDb(), where, null) > 0;
		if(isUpdated){
			Utils.Log("DALShopList- update", item + "");
			boolean isGlobal = item.getGlobalId() != null;
			Long id =  isGlobal ? item.getGlobalId() : item.getLocalId();
			return getItem(id,isGlobal);
		}
		return null;
	}

	public ShopListItem deleteItem(Ids ids){
		boolean isGlobal = ids.getGlobalId() != null;
		Long id =  isGlobal ? ids.getGlobalId() : ids.getDBId();
		ShopListItem item =	  getItem(id,isGlobal);
		String where = ids.getGlobalId() != null ? "UId = '" + item.getGlobalId() + "'" : "_id = " + item.getLocalId();
		boolean res = dbHandler.getWritableDatabase().delete("ShopList", where, null) > 0;
		return item;
	}

	public ShopListItem getItem(long id , boolean isGlobal){
		String idCol  = isGlobal ? "UId" : "_id";
		Cursor c= dbHandler.getReadableDatabase().rawQuery("SELECT * FROM ShopList WHERE "+ idCol+" = " + id, null);
		Utils.Log("DALShopList- getItem", "Num in cursor: " + c.getCount());
		if(c.getCount()>0){
			c.moveToFirst();
			return new ShopListItem(c);
		}
		return null;
	}

	public boolean isItemExist(long globalId){
		Cursor c= dbHandler.getReadableDatabase().rawQuery("SELECT * FROM ShopList WHERE UId = " + globalId, null);
		boolean res = c.getCount() > 0;
		if(!res)
			Utils.LogError("DALShopList","isItemExist", "Item not exist with UId: " + globalId);
		return res;
	}

	public Long getGlobalListId(){
		Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM ShopListOverview WHERE _id = " + listId, null);
		if(c.getCount() > 0){
			c.moveToFirst();
			return c.getLong(c.getColumnIndex("UId"));
		}
		return null;
	}
}
