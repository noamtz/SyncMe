package coupling.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import coupling.app.App;
import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.models.CalenderEvent;

public class DALCalendarEvents {

	private DBHandler dbHandler;

	public DALCalendarEvents(){
		dbHandler = new DBHandler(App.getAppCtx());
	}
	
	public Cursor getSource(){
		return dbHandler.getReadableDatabase().rawQuery("SELECT * FROM CalendarEvents", null);
	}
	
	public long createEvent(Long UId, CalenderEvent calenderEvent, Boolean isMine){
		ContentValues values = new ContentValues();
		values.put("Title", calenderEvent.getTitle());
		values.put("Description", calenderEvent.getDescription());
		values.put("StartTime", calenderEvent.getstartTime());
		values.put("EndTime", calenderEvent.getendTime());
		if(UId != null)
			values.put("UId", UId);
		if(isMine != null)
			values.put("IsMine", isMine);
		
		return dbHandler.getWritableDatabase().insertOrThrow("CalendarEvents", null, values);
	}
	
	public boolean updateEvent(Ids ids, CalenderEvent calenderEvent){
		ContentValues values = new ContentValues();
		if(calenderEvent != null){
			values.put("Title", calenderEvent.getTitle());
			values.put("Description", calenderEvent.getDescription());
			values.put("StartTime", calenderEvent.getstartTime());
			values.put("EndTime", calenderEvent.getendTime());
		}

		if(ids.getGlobalId() != null)
			values.put("UId", ids.getGlobalId());

		String where = (ids.getGlobalId() != null) ? "UId = " + ids.getGlobalId() : "_id = " + ids.getDBId();

		return dbHandler.getWritableDatabase().update("CalendarEvents", values, where, null) > 0;
	}
	
	public boolean deleteEvent(Ids ids){
		String where = ids.getGlobalId() != null ? "UId = '" + ids.getGlobalId() + "'" : "_id = " + ids.getDBId();
		return dbHandler.getWritableDatabase().delete("CalendarEvents", where, null) > 0;
	}
	
	
}
