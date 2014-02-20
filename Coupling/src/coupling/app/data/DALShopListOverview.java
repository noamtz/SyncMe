package coupling.app.data;

import coupling.app.App;
import coupling.app.Utils;
import coupling.app.models.Ids;
import coupling.app.models.ShopListOverView;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Handling all lists data access crud operations
 * @author Noam Tzumie
 *
 */
public class DALShopListOverview {

	private static DALShopListOverview shopListOverviewDAL;
	
	private DBHandler dbHandler;
	
	private DALShopListOverview(){
		dbHandler = new DBHandler(App.getAppCtx());
	}
	
	public static DALShopListOverview getInstance(){
		if(shopListOverviewDAL == null)
			shopListOverviewDAL = new DALShopListOverview();
		return shopListOverviewDAL;
	}
	
	public Cursor getSource(){
		return dbHandler.getReadableDatabase().rawQuery("SELECT * FROM ShopListOverview", null);
	}
	
	
	public boolean updateId(Ids ids){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null) {
			values.put("UId", ids.getGlobalId());
			values.put(Constants.IS_LOCKED, false);
		}
		return dbHandler.getWritableDatabase().update("ShopListOverview",values,"_id = " + ids.getDBId(), null) > 0;
	}
	
	public long createList(ShopListOverView list){
		return dbHandler.getWritableDatabase().insert("ShopListOverview", null, list.toDb());
	}
	
	public boolean updateList(ShopListOverView list){
		return dbHandler.getWritableDatabase().update("ShopListOverview",list.toDb(),"_id = " + list.getLocalId(), null) > 0;
	}
	/**
	 * If increment is true then totalItems++
	 * Else totalItems--
	 * @param increment
	 * @return
	 */
	public boolean updateTotalItems(long id, boolean increment){
		String op = increment ? "+" : "-";
		Cursor c = dbHandler.getWritableDatabase().rawQuery("UPDATE ShopListOverview SET TotalItems = TotalItems " + op + " 1 WHERE _id = " + id, null);
		boolean res =  c.getCount() > 0;
		if(!res)
			Utils.Log("DALShopListOverview", "updateTotalItems", "Failed to " + (increment ? "increment" : "decrement") +" totalItems listid: " + id);
		return res;
	}
	
	public boolean isItemExist(long globalId){
		Cursor c= dbHandler.getReadableDatabase().rawQuery("SELECT * FROM ShopListOverview WHERE UId = " + globalId, null);
		boolean res = c.getCount() > 0;
		if(!res)
			Utils.LogError("DALShopListOverview","isItemExist", "Item not exist with UId: " + globalId);
		return res;
	}
	
	public boolean deleteList(Ids ids){
		String where = ids.getGlobalId() != null ? "UId = '" + ids.getGlobalId() + "'" : "_id = " + ids.getDBId();
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		boolean isDeleted = false;
		db.beginTransaction();
		 try {
			 isDeleted = db.delete("ShopListOverview", where, null) > 0;
			 if(isDeleted)
				 db.delete("ShopList" , "ShopListId = " + ids.getDBId(), null);
			 db.setTransactionSuccessful();
		 } finally { 
			 db.endTransaction();
		 }
		 return isDeleted;
	}

}
