package coupling.app.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import coupling.app.App;
import coupling.app.Utils;
import coupling.app.data.Enums.CategoryType;
import coupling.app.ui.Invite;
import coupling.app.ui.Register;

/**
 * This is Expandble class. this class design to abstract 
 * all the DALs classes but due to time constraints we did'nt do it
 *  
 * @author Noam Tzumie
 *
 */
public class DAL {

	private static DAL dal;
	private DBHandler dbHandler;

	private DAL(){
		dbHandler = new DBHandler(App.getAppCtx());
	}

	public static DAL getInstance(){
		if(dal == null)
			dal = new DAL();
		return dal;
	}
	/**
	 * Getting the local db id of application entity by globalId
	 * @param type
	 * @param GlobalId
	 * @return
	 */
	public Long getLocalId(CategoryType type , Long GlobalId){
		Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM " + getTableName(type) + " WHERE UId = " + GlobalId, null);
		if(c.getCount() > 0){
			c.moveToFirst();
			return c.getLong(c.getColumnIndex("_id"));
		}
		return null;
	}

	public String getTableName(CategoryType type){
		if(type == CategoryType.SHOPLIST)
			return "ShopList";
		if(type == CategoryType.SHOPLIST_OVERVIEW)
			return "ShopListOverview";
		return null;
	}
	
}
