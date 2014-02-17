package coupling.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import coupling.app.Ids;
import coupling.app.Utils;

import static coupling.app.data.Constants.*;
import android.content.ContentValues;
import android.database.Cursor;

public class CalenderEvent extends BaseModel{
	
	private String title;
	private String description;
	private Date startDate;
	private Date startTime;
	private Date endDate;
	private Date  endTime;
	
	public CalenderEvent(){}
	
	public CalenderEvent(Cursor c)
	{
		ids = new Ids(c);
		title =  c.getString(c.getColumnIndexOrThrow(EVENT_TITLE));
		description = c.getString(c.getColumnIndexOrThrow(EVENT_DESCRIPTION));
		isMine = c.getInt(c.getColumnIndexOrThrow(IS_MINE)) == 1;	
		isLocked = c.getInt(c.getColumnIndexOrThrow(IS_LOCKED)) == 1;	
		
		startDate = StringToDate(c.getString(c.getColumnIndexOrThrow(EVENT_START_DATE)), DATE_FORMAT);
		startTime = StringToDate(c.getString(c.getColumnIndexOrThrow(EVENT_START_DATE)), TIME_FORMAT);
		endDate = StringToDate(c.getString(c.getColumnIndexOrThrow(EVENT_START_DATE)), DATE_FORMAT);
		endDate = StringToDate(c.getString(c.getColumnIndexOrThrow(EVENT_START_DATE)), TIME_FORMAT);
	}
	
	public CalenderEvent(String title, String description, Date startDate, Date startTime, Date endDate, Date endTime) {
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
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
	public Date  getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date  geEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = StringToDate(endTime, TIME_FORMAT);
	}
	
	public void setStartTime(String startTime) {
		this.startTime = StringToDate(startTime, TIME_FORMAT);
	}
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = StringToDate(startDate, DATE_FORMAT);
	}
	
	public void setEndDate(String endDate) {
		this.endDate = StringToDate(endDate, DATE_FORMAT);;
	}


	@Override
	public ContentValues toDb() {
		ContentValues values = super.toDb();
		if(title != null)
			values.put(EVENT_TITLE, title);
		if(description != null)
			values.put(EVENT_DESCRIPTION, description);
		if(startTime != null)
			values.put(EVENT_START_TIME, DateToString(startTime, TIME_FORMAT));
		if(description != null)
			values.put(EVENT_END_TIME, DateToString(endTime, TIME_FORMAT));
		if(startDate != null)
			values.put(EVENT_START_DATE, DateToString(startTime, DATE_FORMAT));
		if(endDate != null)
			values.put(EVENT_END_DATE, DateToString(endTime, DATE_FORMAT));
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
			data.put(EVENT_START_TIME, DateToString(startTime, TIME_FORMAT));
		if(description != null)
			data.put(EVENT_END_TIME, DateToString(endTime, TIME_FORMAT));
		if(startDate != null)
			data.put(EVENT_START_DATE, DateToString(startTime, DATE_FORMAT));
		if(endDate != null)
			data.put(EVENT_END_DATE, DateToString(endTime, DATE_FORMAT));
		return data;
	}
	
	//"dd/MM/yyyy HH:mm"
	private String DateToString(Date date, String format){
		SimpleDateFormat simple = new SimpleDateFormat(format, Locale.getDefault());
		return simple.format(date);
	}
	
	public static Date StringToDate(String dateString, String format){
		java.text.DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		try {
			return  df.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	
	
	
}
