package coupling.app.BL;

import org.json.JSONObject;

import android.database.Cursor;

import coupling.app.Ids;
import coupling.app.com.API;
import coupling.app.com.AppFeature;
import coupling.app.com.IBLConnector;
import coupling.app.data.DALCalendarEvents;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.CalenderEvent;

public class BLCalendarEvents extends AppFeature{
	
	IBLConnector connector;
	DALCalendarEvents dataSource;
	API api;

	public BLCalendarEvents(){
		dataSource = new DALCalendarEvents();
		api = API.getInstance();
		categoryType = CategoryType.CALENDER;
	}
	
	public void setBLConnector(IBLConnector connector){
		this.connector = connector;
	}
	public void unsetBLConnector(){
		this.connector = null;
	}
	
	public boolean createEvent(CalenderEvent calendarEvent){
		return createEvent(null, calendarEvent, null, true);
	}
	
	public boolean createEvent(Long UId, CalenderEvent calendarEvent, Boolean isMine, boolean remote){
		return false;
	}
	
	public boolean updateEvent(Ids ids, CalenderEvent calendarEvent){
		return updateEvent(ids, calendarEvent, true);
	}
	
	public boolean updateEvent(Ids ids, CalenderEvent calendarEvent, boolean remote){
		return false;
	}
	
	public boolean deleteEvent(Ids ids){
		return deleteEvent(ids, true);
	}
	
	public boolean deleteEvent(Ids ids, boolean remote){
		return false;
	}
	
	public Cursor getSource(){
		return dataSource.getSource();
	}
	
	@Override
	public void recieveData(JSONObject data, ActionType actionType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateId(Ids ids) {
		// TODO Auto-generated method stub
		return false;
	}

}
