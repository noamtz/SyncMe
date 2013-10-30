package syncme.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DataSource {

	// Database fields
	private SQLiteDatabase database;
	private DBHandler dbHelper;
//	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
//			MySQLiteHelper.COLUMN_COMMENT };

	public DataSource(Context context) {
		dbHelper = new DBHandler(context);
	}

//	public Comment create(String comment) {
//	    ContentValues values = new ContentValues();
//	    values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
//	    long insertId = database.insert(table, nullColumnHack, values)
//	    Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
//	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
//	        null, null, null);
//	    cursor.moveToFirst();
//	    Comment newComment = cursorToComment(cursor);
//	    cursor.close();
//	    return newComment;
//	  }
	
}
