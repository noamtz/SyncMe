package coupling.app.data;

import coupling.app.App;
import coupling.app.Utils;
import coupling.app.models.Ids;
import coupling.app.models.ShopListItem;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

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
		 long id = dbHandler.getWritableDatabase().update("ShopList",item.toDb(), where, null);
		 if(id > 0)
			 return getItem(item.getLocalId());
		 return null;
	}

	public ShopListItem deleteItem(Ids ids){
		ShopListItem item = getItem(ids.getDBId());
		String where = ids.getGlobalId() != null ? "UId = '" + item.getGlobalId() + "'" : "_id = " + item.getLocalId();
		boolean res = dbHandler.getWritableDatabase().delete("ShopList", where, null) > 0;
		return item;
	}

	public ShopListItem getItem(long id){
		Cursor c= dbHandler.getReadableDatabase().rawQuery("SELECT * FROM ShopList WHERE _id = " + id, null);
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
