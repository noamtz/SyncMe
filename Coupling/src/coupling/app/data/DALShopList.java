package coupling.app.data;

import coupling.app.App;
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
	
	public boolean addItem(String name, int quantity){
		ContentValues values = new ContentValues();
		values.put("ShopListId", listId);
		values.put("ItemName", name);
		values.put("ItemQuantity", quantity);
		Log.v("dal_shoplist","id: " + listId + " name: " + name + " quantity: "+ quantity);
		return dbHandler.getWritableDatabase().insertOrThrow("ShopList", null, values) != -1;
	}
	
	public boolean updateItem(long id, String name, Integer quantity, Boolean isDone){
		ContentValues values = new ContentValues();
		if(name != null)
			values.put("ItemName", name);
		if(quantity != null)
			values.put("ItemQuantity", quantity);
		if(isDone != null)
			values.put("ItemStatus", isDone);
	
		return dbHandler.getWritableDatabase().update("ShopList",values,"_id = " + id, null) > 0;
	}
	
	public boolean deleteItem(long id){
		 return dbHandler.getWritableDatabase().delete("ShopList", "_id = " + id, null) > 0;
	}
}
