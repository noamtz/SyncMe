package coupling.app.BL;

import static coupling.app.data.Constants.*;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.com.API;
import coupling.app.com.IBLConnector;
import coupling.app.com.Message;
import coupling.app.data.DALCalendarEvents;
import coupling.app.data.Enums.ActionType;
import coupling.app.data.Enums.CategoryType;
import coupling.app.models.CalenderEvent;
import coupling.app.models.ShopListItem;

public class BLCalendarEvents extends AppFeature{
	
	IBLConnector connector;
	DALCalendarEvents dataSource;
	API api;

	public BLCalendarEvents(){
		dataSource = DALCalendarEvents.getInstance();
		api = API.getInstance();
		categoryType = CategoryType.CALENDER;
	}
	
	//get all info of all the events
	public Cursor getSource(){
		return dataSource.getSource();
	}
	
	//add new event
	public boolean createEvent(String title, String description, String startDate, String startTime, String endDate, String endTime){
		return createEvent(new CalenderEvent(title, description, startDate, startTime, endDate, endTime), true);
	}
	
	//add new event
	public boolean createEvent(CalenderEvent calendarEvent, boolean remote){
		long localId = dataSource.createEvent(calendarEvent);
		
		boolean isCreated = (localId != -1); 

		//sync with friend
		if(remote && isCreated){
			Message message = new Message();

			message.setData(calendarEvent.toNetwork());
			message.getData().put(LOCALID, localId);
			message.getData().put(UID, calendarEvent.getGlobalId());

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.CREATE);

			api.sync(message);
		}
		return isCreated;
	}
	
	//update a event
	public boolean updateEvent(Ids ids, CalenderEvent calendarEvent){
		return updateEvent(calendarEvent, true);
	}
	
	//update a event
	public boolean updateEvent(CalenderEvent calendarEvent, boolean remote){
		boolean isUpdated = dataSource.updateEvent(calendarEvent);
		
		//sync with friend
		if(remote && isUpdated){
			Message message = new Message();
			
			message.setData(calendarEvent.toNetwork());

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.UPDATE);
			api.sync(message);
		}

		return isUpdated;
	}
	
	//delete a event
	public boolean deleteEvent(Ids ids){
		return deleteEvent(ids, true);
	}
	
	//delete a event
	public boolean deleteEvent(Ids ids, boolean remote){
		boolean isDeleted = dataSource.deleteEvent(ids);
		
		//sync with friend
		if(remote && isDeleted){
			Message message = new Message();

			message.getData().put(UID, ids.getGlobalId());
			message.getData().put(LOCALID, ids.getDBId());

			message.setCategoryType(categoryType);
			message.setActionType(ActionType.DELETE);

			api.sync(message);
		}

		return isDeleted;
	}
	
	//receive the event from friend
	@Override
	public synchronized void recieveData(JSONObject data, ActionType actionType) {
		try{
			CalenderEvent event = new CalenderEvent();
			if(data.has(UID) && !data.get(UID).equals(null))
				event.getIds().setGlobalId(data.getLong(UID));
			
			//Unlock item
			event.setIsLocked(false);

			if (data.has(EVENT_TITLE)) event.setTitle(data.getString(EVENT_TITLE));
			if (data.has(EVENT_DESCRIPTION)) event.setDescription(data.getString(EVENT_DESCRIPTION));
			if (data.has(EVENT_START_TIME)) event.setStartTime(data.getString(EVENT_START_TIME));
			if (data.has(EVENT_END_TIME)) event.setEndTime(data.getString(EVENT_END_TIME));
			if (data.has(EVENT_START_DATE)) event.setStartDate(data.getString(EVENT_START_DATE));
			if (data.has(EVENT_END_DATE)) event.setEndDate(data.getString(EVENT_END_DATE));

			switch (actionType) {
			case CREATE:
				event.setIsMine(false);
				createEvent(event, false);
				break;

			case UPDATE:	
				updateEvent(event, false);
				break;
			case DELETE:
				if(dataSource.isItemExist(event.getIds().getGlobalId())){
					deleteEvent(event.getIds(), false);
				}
				break;
			}
			if(connector == null)
				super.recieveData(data, actionType);
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		if(connector != null)
			connector.Refresh();
		
	}

	//update event id
	@Override
	public boolean updateId(Ids ids) {
		if(dataSource.updateId(ids)){
			if(connector != null){
				connector.Refresh();
				return true;
			}else {
				//Utils.showToast("Not connected to Calendar");
			}
		} else {
			//Utils.showToast("Failed to update GlobalId for event");
		}
		return false;
	}

	public void setBLConnector(IBLConnector connector){
		this.connector = connector;
	}
	
	public void unsetBLConnector(){
		this.connector = null;
	}
	
	//get all events of specific date
	public Cursor getDayEvents(String date){
		return dataSource.getDayEvents(date);
	}
}
