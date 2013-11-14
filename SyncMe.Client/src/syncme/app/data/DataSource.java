package syncme.app.data;

import syncme.app.App;
import syncme.app.utils.CommonUtils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

	protected SQLiteDatabase database;
	protected DBHandler dbHandler;
	protected String tableName;

	private static final String TAG = "DataSource";
	
	public DataSource(DBHandler dbHandler, String tableName) {
		this.dbHandler = dbHandler;
		this.tableName = tableName;
		//TODO: begin\end transaction
		open();
	}

	public void open() throws SQLException {
		database = dbHandler.getWritableDatabase();
	}

	public void close() {
		dbHandler.close();
	}
	/**
	 * Create new row in the database 
	 * @return auto increment id
	 */
	public long create(ContentValues values){
		//TODO: handle create exceptions.
		long id = database.insert(tableName, null,values);
		CommonUtils.Log(TAG, "create", "Inserted into " + tableName + " id: " + id);
		return id;
	}


	public boolean delete(long id)
	{
		//TODO: handle delete exceptions.
		return database.delete(tableName, String.format("id=%d", id) , null) > 0;
	}

	public boolean update (ContentValues values, String whereClause, String[] whereArgs){
		//TODO: handle update exceptions.
		int numRows = database.update(tableName,values,whereClause, null);
		return numRows > 0;
	}
	
	/**
	 * only child of this class can perfom this method
	 * Usage: ("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"})
	 * @param query
	 * @param args
	 * @return
	 */
	protected Cursor rawQuery(String query, String[] args){
		return database.rawQuery(query, args);
	}


	public Cursor getAllRecords(){ 
		return rawQuery("SELECT * FROM " + tableName, null);
	}

	public Cursor getRecord(long id){ 
		return rawQuery("SELECT * FROM " +tableName+ " WHERE id = ?", new String[] { "" + id });
	}
	
	public Cursor getRecord(String colName, String val){ 
		return rawQuery("SELECT * FROM " +tableName+ " WHERE ? = ?", new String[] { colName, val});
	}
}
