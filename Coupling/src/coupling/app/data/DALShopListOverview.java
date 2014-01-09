package coupling.app.data;

import coupling.app.App;
import coupling.app.Ids;
import android.content.ContentValues;
import android.database.Cursor;

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
	
	public long createList(Long UId, String title){
		ContentValues values = new ContentValues();
		values.put("Title", title);
		if(UId != null)
			values.put("UId", UId);
		
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
	
	public boolean updateId(Ids ids){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null)
			values.put("UId", ids.getGlobalId());
		return dbHandler.getWritableDatabase().update("ShopListOverview",values,"_id = " + ids.getDBId(), null) > 0;
	}
}
