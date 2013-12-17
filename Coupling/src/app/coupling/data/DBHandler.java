package app.coupling.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

	public DBHandler(Context context) {
		super(context,"coupling.db", null, 6);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(
				"CREATE TABLE ShopListOverview ( " +
							"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"Title VARCHAR(50), " +
							"TotalItems Int DEFAULT 0, " +
							"CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP " +
						");"
				);
		database.execSQL(
				"CREATE TABLE ShopList ( " +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"ShopListId INTEGER, " +
						"ItemName VARCHAR(50), " +
						"ItemQuantity INTEGER DEFAULT 1, " +
						"ItemStatus INTEGER DEFAULT 0, " +
						"FOREIGN KEY(ShopListId) REFERENCES ShopListOverview(_id)" +
				");"
			);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHandler.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS ShopList");
		db.execSQL("DROP TABLE IF EXISTS ShopListOverview");
		onCreate(db);
	}
} 