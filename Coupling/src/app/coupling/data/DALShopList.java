package app.coupling.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import app.coupling.App;

public class DALShopList {

	private static DALShopList shoplListDAL;
	
	private DBHandler dbHandler;
	
	private DALShopList(){
		dbHandler = new DBHandler(App.getAppCtx());
	}
	
	public static DALShopList getInstance(){
		if(shoplListDAL == null)
			shoplListDAL = new DALShopList();
		return shoplListDAL;
	}
	
	public Cursor getSource(long shopListid){
		return dbHandler.getWritableDatabase().rawQuery("SELECT * FROM ShopList WHERE ShopListId = " + shopListid, null);
	}
	
	public boolean addItem(long listId, String name, int quantity){
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
}
