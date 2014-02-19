package coupling.app.data;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import coupling.app.App;
import coupling.app.Utils;
import coupling.app.com.NetworkRequestQueue;


public class DALNetworkQueue {
	

	
	private static DALNetworkQueue networkQueueDAL;

	private static final String TABLE_NAME = "NetworkQueue";
	
	private DBHandler dbHandler;

	private DALNetworkQueue(){
		dbHandler = new DBHandler(App.getAppCtx());
	}

	public static DALNetworkQueue getInstance(){
		if(networkQueueDAL == null)
			networkQueueDAL = new DALNetworkQueue();
		return networkQueueDAL;
	}

	public boolean add(JSONObject json){
		ContentValues values = new ContentValues();
		values.put("RequestData", json.toString());
		boolean res = dbHandler.getWritableDatabase().insertOrThrow(TABLE_NAME, null, values) != -1;
		Utils.Log("DALNetworkQueue","add", " isSuccess: " + res);
		return res;
	}
	
	public boolean remove(long id){
		boolean res =  dbHandler.getWritableDatabase().delete(TABLE_NAME, "_id="+id, null) > 0;
		Utils.Log("DALNetworkQueue","remove", " isSuccess: " + res);
		return res;
	}
	
	public void fillQueue(){
		Cursor c = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
		Utils.Log("DALNetworkQueue","getAll", " cursor count: " + c.getCount());
		Map<Long,JSONObject> dbQueue = NetworkRequestQueue.getInstance().getDBQueue();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndexOrThrow("_id"));
			String json = c.getString(c.getColumnIndexOrThrow("RequestData"));
			if(!dbQueue.containsKey(id))
				try {
					dbQueue.put(id, new JSONObject(json));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
	}
	
}
