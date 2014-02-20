package coupling.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import coupling.app.App;
import coupling.app.Utils;
import coupling.app.models.CalenderEvent;
import coupling.app.models.Ids;
import static coupling.app.data.Constants.*;

public class DALCalendarEvents {

	private static DALCalendarEvents calederEventsDAL;
	private DBHandler dbHandler;

	public DALCalendarEvents(){
		dbHandler = new DBHandler(App.getAppCtx());
	}

	public static DALCalendarEvents getInstance(){
		if(calederEventsDAL == null)
			calederEventsDAL = new DALCalendarEvents();
		return calederEventsDAL;
	}

	public Cursor getSource(){
		return dbHandler.getReadableDatabase().rawQuery("SELECT * FROM CalendarEvents", null);
	}

	public boolean updateId(Ids ids){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null) {
			values.put("UId", ids.getGlobalId());
			values.put(Constants.IS_LOCKED, false);
		}
		return dbHandler.getWritableDatabase().update("CalendarEvents", values, "_id = " + ids.getDBId(), null) > 0;
	}

	public long createEvent(CalenderEvent event){
		Log.w("BBB",  event.toDb().toString());
		return dbHandler.getWritableDatabase().insertOrThrow("CalendarEvents", null, event.toDb());
	}

	public boolean updateEvent(CalenderEvent event) {
		String where = (event.getGlobalId() != null) ? "UId = " + event.getGlobalId() : "_id = " + event.getLocalId();
		return dbHandler.getWritableDatabase().update("CalendarEvents", event.toDb(), where, null) > 0;
	}

	public boolean deleteEvent(Ids ids){
		String where = ids.getGlobalId() != null ? "UId = '" + ids.getGlobalId() + "'" : "_id = " + ids.getDBId();
		return dbHandler.getWritableDatabase().delete("CalendarEvents", where, null) > 0;
	}


	public boolean isItemExist(long globalId){
		Cursor c= dbHandler.getReadableDatabase().rawQuery("SELECT * FROM CalendarEvents WHERE UId = " + globalId, null);
		boolean res = c.getCount() > 0;
		if(!res)
			Utils.LogError("DALCalendarEvents","isItemExist", "Item not exist with UId: " + globalId);
		return res;
	}

	public Cursor getDayEvents(String date){
		return dbHandler.getReadableDatabase().rawQuery("SELECT * FROM CalendarEvents WHERE " + EVENT_START_DATE + " = '" + date + "'" , null);
	}
}
