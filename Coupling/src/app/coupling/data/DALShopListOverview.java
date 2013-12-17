package app.coupling.data;

import android.content.ContentValues;
import android.database.Cursor;
import app.coupling.App;

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
		return dbHandler.getWritableDatabase().rawQuery("SELECT * FROM ShopListOverview", null);
	}
	
	public boolean addList(String title){
		ContentValues values = new ContentValues();
		values.put("Title", title);
		
		return dbHandler.getWritableDatabase().insert("ShopListOverview", null, values) != -1;
	}
	
	public boolean updateList(long id, String title, Integer totalItems){
		ContentValues values = new ContentValues();
		if(title != null)
			values.put("Title", title);
		if(totalItems != null)
			values.put("TotalItems", totalItems);
		
		return dbHandler.getWritableDatabase().update("ShopListOverview",values,"_id = " + id, null) > 0;
	}
}
