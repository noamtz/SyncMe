package coupling.app.data;

import coupling.app.App;
import coupling.app.Ids;
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
		return dbHandler.getWritableDatabase().rawQuery("SELECT * FROM ShopList WHERE ShopListId = " + listId, null);
	}
	
	public boolean createItem(String UId, String name, int quantity){
		ContentValues values = new ContentValues();
		values.put("ShopListId", listId);
		values.put("ItemName", name);
		values.put("ItemQuantity", quantity);
		if(UId != null)
			values.put("UId", UId);
		Log.v("dal_shoplist","id: " + listId + " name: " + name + " quantity: "+ quantity);
		return dbHandler.getWritableDatabase().insertOrThrow("ShopList", null, values) != -1;
	}
	
	public boolean updateItem(Ids ids, String name, Integer quantity, Boolean isDone){
		ContentValues values = new ContentValues();
		if(name != null)
			values.put("ItemName", name);
		if(quantity != null)
			values.put("ItemQuantity", quantity);
		if(isDone != null)
			values.put("ItemStatus", isDone);
		
		String where = ids.getGlobalId() != null ? "Uid = '" + ids.getGlobalId() + "'" : "_id = " + ids.getDBId();
		
		return dbHandler.getWritableDatabase().update("ShopList",values, where, null) > 0;
	}
	
	public boolean deleteItem(Ids ids){
		 String where = ids.getGlobalId() != null ? "Uid = '" + ids.getGlobalId() + "'" : "_id = " + ids.getDBId();
		 return dbHandler.getWritableDatabase().delete("ShopList", where, null) > 0;
	}
	
	public boolean updateId(Ids ids){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null)
			values.put("UId", ids.getGlobalId());
		return dbHandler.getWritableDatabase().update("ShopList",values,"_id = " + ids.getDBId(), null) > 0;
	}
}
