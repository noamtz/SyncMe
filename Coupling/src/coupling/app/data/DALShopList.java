package coupling.app.data;

import coupling.app.App;
import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.models.ShopListItem;
import android.content.ContentValues;
import android.database.Cursor;

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

	public long createItem(ShopListItem item){
		long itemId = dbHandler.getWritableDatabase().insertOrThrow("ShopList", null, item.toDb());
		if(itemId != -1)
			DALShopListOverview.getInstance().updateTotalItems(listId, true);
		else
			Utils.LogError("DALShopList", "createItem", "createItem");
		return itemId;
	}

	public boolean updateItem(ShopListItem item) {

		String where = (item.getGlobalId() != null) ? "UId = " + item.getGlobalId() : "_id = " + item.getLocalId();
		Utils.Log("DAL", "Update", where);
		return dbHandler.getWritableDatabase().update("ShopList",item.toDb(), where, null) > 0;
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
