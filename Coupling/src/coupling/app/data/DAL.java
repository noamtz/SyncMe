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

	public boolean storeFriend(JSONObject friend){
		boolean result = true;
		try {
			if(friend.has("error")){
				Utils.showToast(friend.getString("error"));
				result = false;
			} else {
				SharedPreferences prefs = App.getAppCtx().getSharedPreferences(Invite.class.getSimpleName(),
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(Constants.FRIEND_NAME, friend.getString("friendName"));

				editor.commit();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean addToNetorkQueue(JSONObject json){
		ContentValues values = new ContentValues();
		values.put("RequestData", json.toString());
		return dbHandler.getWritableDatabase().insertOrThrow("NetworkQueue", null, values) != -1;
	}
}
