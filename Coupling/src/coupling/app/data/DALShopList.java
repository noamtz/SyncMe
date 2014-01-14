package coupling.app.data;

import coupling.app.App;
import coupling.app.Ids;
import coupling.app.Utils;
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

	public long createItem(Long UId, String name, int quantity, Boolean isMine){
		ContentValues values = new ContentValues();
		values.put("ShopListId", listId);
		values.put("ItemName", name);
		values.put("ItemQuantity", quantity);
		if(UId != null)
			values.put("UId", UId);
		if(isMine != null)
			values.put("IsMine", isMine);
		Log.v("dal_shoplist","id: " + listId + " name: " + name + " quantity: "+ quantity);
		long itemId = dbHandler.getWritableDatabase().insertOrThrow("ShopList", null, values);
		if(itemId != -1)
			DALShopListOverview.getInstance().updateTotalItems(listId, true);
		return itemId;
	}

	public boolean updateItem(Ids ids, String name, Integer quantity, Boolean isDone){
		ContentValues values = new ContentValues();
		if(name != null)
			values.put("ItemName", name);
		if(quantity != null)
			values.put("ItemQuantity", quantity);
		if(isDone != null)
			values.put("ItemStatus", isDone);
		if(ids.getGlobalId() != null)
			values.put("UId", ids.getGlobalId());

		Utils.Log("DAL", "Update", "Global: " + ids.getGlobalId());

		String where = (ids.getGlobalId() != null) ? "UId = " + ids.getGlobalId() : "_id = " + ids.getDBId();
		Utils.Log("DAL", "Update", where);
		return dbHandler.getWritableDatabase().update("ShopList",values, where, null) > 0;
	}

	public boolean deleteItem(Ids ids){
		String where = ids.getGlobalId() != null ? "UId = '" + ids.getGlobalId() + "'" : "_id = " + ids.getDBId();
		boolean res = dbHandler.getWritableDatabase().delete("ShopList", where, null) > 0;
		if(res)
			DALShopListOverview.getInstance().updateTotalItems(listId, false);
		return res;
	}

	public boolean updateId(Ids ids){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null)
			values.put("UId", ids.getGlobalId());
		return dbHandler.getWritableDatabase().update("ShopList",values,"_id = " + ids.getDBId(), null) > 0;
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
