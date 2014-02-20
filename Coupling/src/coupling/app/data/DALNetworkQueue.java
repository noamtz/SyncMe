package coupling.app.data;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import coupling.app.App;
import coupling.app.Utils;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.NetworkOfflineItem;
import static coupling.app.data.Constants.*;

public class DALNetworkQueue {

	private static DALNetworkQueue networkQueueDAL;

	private static final String TABLE_NAME = "NetworkQueue";

	private DBHandler dbHandler;

	private DALNetworkQueue(){
		dbHandler = new DBHandler(App.getAppCtx());
	}

	public static synchronized DALNetworkQueue getInstance(){
		if(networkQueueDAL == null)
			networkQueueDAL = new DALNetworkQueue();
		return networkQueueDAL;
	}

	public boolean add(JSONObject json, CategoryType type, Long dbId){
		ContentValues values = new ContentValues();
		values.put("RequestData", json.toString());
		values.put("Type", type.value());
		values.put("DbId", dbId);
		long networkId = dbHandler.getWritableDatabase().insertOrThrow(TABLE_NAME, null, values);
		Utils.Log("DALNetworkQueue","add", " networkId: " + networkId + " for itemId: " + dbId);
		return networkId != -1;
	}

	public long isAppItemExist(CategoryType type, Long dbId){
		Cursor c = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM NetworkQueue WHERE Type = " + type.value() + " AND dbId = " + dbId, null);
		if(c.getCount() > 0){
			c.moveToFirst();
			return c.getLong(c.getColumnIndex("_id"));
		}
		return -1;
	}

	public boolean remove(long id){
		boolean res =  dbHandler.getWritableDatabase().delete(TABLE_NAME, "_id="+id, null) > 0;
		Utils.Log("DALNetworkQueue","remove", " isSuccess: " + res + " for networkId: " + id);
		return res;
	}

	public ArrayList<NetworkOfflineItem> getQueue(){
		Cursor c = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
		Utils.Log("DALNetworkQueue","getAll", " cursor count: " + c.getCount());
		ArrayList<NetworkOfflineItem> dbQueue = new ArrayList<NetworkOfflineItem>();
		while (c.moveToNext()) {
			long id = c.getLong(c.getColumnIndexOrThrow("_id"));
			String json = c.getString(c.getColumnIndexOrThrow("RequestData"));
			NetworkOfflineItem item = new NetworkOfflineItem(id, json);
			dbQueue.add(item);
		}
		return dbQueue;
	}

	public Long retrieveGlobalId(Long localId, String tableName){
		Long res = new Long(0);
		Cursor c = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM " + tableName + " WHERE " + ID + "=" + localId, null);
		if(c.getCount() == 1){
			c.moveToFirst();
			res = c.getLong(c.getColumnIndex(UID));
		}
		return res;
	}

	public Long retrieveGlobalListId(Long localListId){
		Long res = new Long(0);
		Cursor c = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM ShopListOverview WHERE _id = " + localListId, null);
		if(c.getCount() > 0){
			c.moveToFirst();
			res = c.getLong(c.getColumnIndexOrThrow("UId"));
		}
		return res;
	}

}
