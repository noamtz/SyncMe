package syncme.app.data;

import java.util.ArrayList;

import syncme.app.utils.CommonUtils;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static syncme.app.data.DBConstants.*;

public class DBHandler extends SQLiteOpenHelper {

	public DBHandler(Context context) {
		super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		ArrayList<String> script = CommonUtils.getTablesQuery();
		for(String table : script){
			CommonUtils.Log("db",table );
			CommonUtils.Log("db","");
			CommonUtils.Log("db","");
			CommonUtils.Log("db","");
			execSQL(database, table);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHandler.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP VIEW IF EXISTS " + VIEW_SHOP_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPLIST_OVERVIEW);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		onCreate(db);
	}


	private void execSQL(SQLiteDatabase database, String script){
		try{
			database.execSQL(script);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
} 