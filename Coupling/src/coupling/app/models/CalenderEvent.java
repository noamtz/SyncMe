package coupling.app.models;


import java.util.Map;

import coupling.app.Ids;

import static coupling.app.data.Constants.*;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class CalenderEvent extends BaseModel{
	
	private String title;
	private String description;
	private String startDate;
	private String startTime;
	private String endDate;
	private String  endTime;	

	
	public CalenderEvent(){}
	
	public CalenderEvent(Cursor c)
	{
		ids = new Ids(c);

		title =  c.getString(c.getColumnIndexOrThrow(EVENT_TITLE));
		description = c.getString(c.getColumnIndexOrThrow(EVENT_DESCRIPTION));
		isMine = c.getInt(c.getColumnIndexOrThrow(IS_MINE)) == 1;	
		isLocked = c.getInt(c.getColumnIndexOrThrow(IS_LOCKED)) == 1;	
		startDate = c.getString(c.getColumnIndexOrThrow(EVENT_START_DATE));
		startTime = c.getString(c.getColumnIndexOrThrow(EVENT_START_TIME));
		endDate = c.getString(c.getColumnIndexOrThrow(EVENT_END_DATE));
		endTime = c.getString(c.getColumnIndexOrThrow(EVENT_END_TIME));
	}
	
	public CalenderEvent(String title, String description, String startDate, String startTime, String endDate, String endTime) {
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
	}
	
	public String getEndTime() {
		return endTime;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String  getStartTime() {
		return startTime;
	}

	public String  geEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	@Override
	public ContentValues toDb() {
		ContentValues values = super.toDb();
		if(title != null)
			values.put(EVENT_TITLE, title);
		if(description != null)
			values.put(EVENT_DESCRIPTION, description);
		if(startTime != null)
			values.put(EVENT_START_TIME, startTime);
		if( endTime != null)
			values.put(EVENT_END_TIME, endTime);
		if(startDate != null)
			values.put(EVENT_START_DATE, startDate);
		if(endDate != null)
			values.put(EVENT_END_DATE, endDate);
		Log.w("ttt", values.toString());
		return values;
	}


	@Override
	public Map<String, Object> toNetwork() {
		Map<String,Object> data = super.toNetwork();
		if(title != null)
			data.put(EVENT_TITLE, title);
		if(description != null)
			data.put(EVENT_DESCRIPTION, description);
		if(startTime != null)
			data.put(EVENT_START_TIME, startTime);
		if(endTime != null)
			data.put(EVENT_END_TIME, endTime);
		if(startDate != null)
			data.put(EVENT_START_DATE, startDate);
		if(endDate != null)
			data.put(EVENT_END_DATE, endDate);
		return data;
	}	
	
}
