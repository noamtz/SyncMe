package coupling.app.data;

import coupling.app.App;
import coupling.app.Ids;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	
	public long createList(Long UId, String title , Boolean isMine){
		ContentValues values = new ContentValues();
		values.put("Title", title);
		if(UId != null)
			values.put("UId", UId);
		if(isMine != null)
			values.put("IsMine", isMine);
		
		return dbHandler.getWritableDatabase().insert("ShopListOverview", null, values);
	}
	
	public boolean updateList(long id, String title, Integer totalItems){
		ContentValues values = new ContentValues();
		if(title != null)
			values.put("Title", title);
		if(totalItems != null)
			values.put("TotalItems", totalItems);
		
		return dbHandler.getWritableDatabase().update("ShopListOverview",values,"_id = " + id, null) > 0;
	}
	
	
	public boolean deleteList(Ids ids){
		String where = ids.getGlobalId() != null ? "UId = '" + ids.getGlobalId() + "'" : "_id = " + ids.getDBId();
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		boolean isDeleted = false;
		db.beginTransaction();
		 try {
			 isDeleted = db.delete("ShopListOverview", where, null) > 0;
			 if(isDeleted)
				 isDeleted = db.delete("ShopList" , "ShopListId = " + ids.getDBId(), null) > 0;
			 db.setTransactionSuccessful();
		 } finally { 
			 db.endTransaction();
		 }
		 return isDeleted;
	}
	
	public boolean updateId(Ids ids){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null)
			values.put("UId", ids.getGlobalId());
		return dbHandler.getWritableDatabase().update("ShopListOverview",values,"_id = " + ids.getDBId(), null) > 0;
	}
}
