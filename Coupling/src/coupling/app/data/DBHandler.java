package coupling.app.data;

import coupling.app.GroceryList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static coupling.app.data.Constants.*;

public class DBHandler extends SQLiteOpenHelper {
	Thread thread;
	
	public DBHandler(Context context) {
		super(context,"coupling.db", null, 30);	
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(
				"CREATE TABLE ShopListOverview ( " +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"UId INTEGER NULL," + 
						"Title VARCHAR(50), " +
						"TotalItems Int DEFAULT 0, " +
						"IsMine INTEGER DEFAULT 1, " +
						"IsLocked INTEGER DEFAULT 1, " +
						"CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP " +
						");"
				);
		database.execSQL(
				"CREATE TABLE ShopList ( " +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"UId INTEGER ," + 
						"ShopListId INTEGER, " +
						"ItemName VARCHAR(50), " +
						"ItemQuantity INTEGER DEFAULT 1, " +
						"IsDone INTEGER DEFAULT 0, " +
						"IsMine INTEGER DEFAULT 1, " +
						"IsLocked INTEGER DEFAULT 1, " +
						"FOREIGN KEY(ShopListId) REFERENCES ShopListOverview(_id)" +
						");"
				);
		database.execSQL(
				"CREATE TABLE GroceryList ( " +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"ItemName VARCHAR(50)" + 
						");"
				);
		database.execSQL(
				"CREATE TABLE CalendarEvents ( " +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"UId INTEGER ," + 
						EVENT_TITLE +" VARCHAR(100), " +
						EVENT_DESCRIPTION + " VARCHAR(100), " +
						EVENT_START_DATE + " VARCHAR(50), " +
						EVENT_START_TIME + " VARCHAR(50), " +
						EVENT_END_DATE + " VARCHAR(50), " +
						EVENT_END_TIME + " VARCHAR(50), " +
						"IsMine INTEGER DEFAULT 1, " +
						"IsLocked INTEGER DEFAULT 1 " +
						");"
				);
		database.execSQL(
				"CREATE TABLE NetworkQueue ( " +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"RequestData VARCHAR(500)," +
						"Type INTEGER," +
						"DbId INTEGER" +
						");"
				);
		insertItemsSQLThread();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHandler.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS ShopList");
		db.execSQL("DROP TABLE IF EXISTS ShopListOverview");
		db.execSQL("DROP TABLE IF EXISTS GroceryList");
		db.execSQL("DROP TABLE IF EXISTS CalendarEvents");
		db.execSQL("DROP TABLE IF EXISTS NetworkQueue");
		onCreate(db);
	}

	private void insertItemsSQLThread(){

		thread = new Thread(){

			@Override
			public void run() {
				GroceryList gl = GroceryList.getInstance();
				gl.initGroceryListItems();
				super.run();
			}

		};
		thread.start();
	}
} 