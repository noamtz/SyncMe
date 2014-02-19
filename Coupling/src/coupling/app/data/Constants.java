package coupling.app.data;

public class Constants {

	/* API */
	public static final String METHOD = "method";
	public static final String PARAMS = "params";
	public static final String REGISTER = "register";
	public static final String SYNC = "sync";
	public static final String INVITE = "registerFriend";
	public static final String MESSAGE_RECIEVED = "messageRecieved";
	public static final String MESSAGE_ID = "messageId";
	public static final String GET_MESSAGE = "getMessage";
	public static final String FRIEND_NAME = "friendName";
	
	/* SERVER PARAMS */
	public static final String FRIEND_EMAIL = "friendEmail";
	public static final String EMAIL = "email";
	public static final String MESSAGE = "message";
	
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String IS_REGISTERED = "isRegistered";
	public static final String REG_ID = "regId";
	public static final String APP_VERSION = "appVersion";
	
	public static final int NUM_OF_SCREENS = 4;
	public static final int POSITION_SHOPLIST = 0;
	public static final int POSITION_CALENDER = 1;
	public static final int POSITION_TODO = 2;
	public static final int POSITION_LIBRARY = 3;
	
	public static final String UNIMPLEMENTED_MSG = "unimplemented method";

	//Database
	public static final String ID = "_id";
	
	//App Features

	public static final String LOCALID = "LocalId";
	public static final String UID = "UId";
	public static final String IS_MINE = "IsMine";
	public static final String IS_LOCKED = "IsLocked";
	
	
	//Shop List Overview
	public static final String TITLE = "Title";
	public static final String TOTAL_ITEMS = "TotalItems";
	public static final String CREATED_AT = "CreatedAt";
	
	//Shop List Item
	public static final String LOCAL_LIST_ID = "LocalListId";
	public static final String GLOBAL_LIST_ID = "GlobalListId";
	public static final String ITEM_NAME = "ItemName";
	public static final String ITEM_QUANTITY = "ItemQuantity";
	public static final String IS_DONE = "IsDone";
	public static final String SHOPLIST_ID = "ShopListId";
	
	
	//Calender Event
	public static final String CALENDER_PREFS = "calender_prefs";
	public static final String EVENT_ID= "event_id";
	public static final long EVENT_CREATE= 0;
	public static final long EVENT_UPDATE= 1;
	public static final String EVENT_YEAR = "year";
	public static final String EVENT_MONTH = "month";
	public static final String EVENT_DAY = "day";
	public static final String SELECTED_DATE = "selected_day";
	
	public static final String EVENT_TITLE = "eventTitle";
	public static final String EVENT_DESCRIPTION = "eventDescription";
	public static final String EVENT_START_TIME = "eventStartTime";
	public static final String EVENT_END_TIME = "eventEndTime";
	public static final String EVENT_START_DATE = "eventStartDate";
	public static final String EVENT_END_DATE = "eventEndDate";
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String TIME_FORMAT = "HH:mm";

	//Notifications
	public static final int NOTIFICATION_ID = 1;
	public static final String NOTIF_TITLE = "Coupling";
	
	
}
