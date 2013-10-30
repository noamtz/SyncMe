package syncme.app.data;

import syncme.app.utils.CommonUtils;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

	public DBHandler(Context context) {
		super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String script = CommonUtils.getStringFromAssetFile(DBConstants.INIT_DB_SCRIPT_PATH);
		execSQL(database, script);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHandler.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		//    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
		//    onCreate(db);
	}


	private void execSQL(SQLiteDatabase database, String script){
		try{
			database.execSQL(script);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
} 