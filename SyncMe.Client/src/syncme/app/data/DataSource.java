package syncme.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

	protected SQLiteDatabase database;
	protected DBHandler dbHelper;
	protected String tableName;

	public DataSource(DBHandler dbHelper, String tableName) {
		dbHelper = this.dbHelper;
		this.tableName = tableName;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	/**
	 * Create new row in the database 
	 * @return auto increment id
	 */
	public long create(ContentValues values){
		//TODO: handle create exceptions.
		database.beginTransaction();
		long id = database.insert(tableName, null,values);
		database.endTransaction();
		return id;
	}


	public void delete(long id)
	{
		//TODO: handle delete exceptions.
		database.delete(tableName, String.format("id=%d", id) , null);
	}

	public boolean update (ContentValues values, String whereClause, String[] whereArgs){
		//TODO: handle update exceptions.
		database.beginTransaction();
		int numRows = database.update(tableName,values,whereClause, null);
		database.endTransaction();
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
		return rawQuery("SELECT * FROM ?", new String[] { tableName });
	}

	public Cursor getRecord(long id){ 
		return rawQuery("SELECT * FROM ? WHERE id = ?", new String[] { tableName, "" + id });
	}
	
	public Cursor getRecord(String colName, String val){ 
		return rawQuery("SELECT * FROM ? WHERE ? = ?", new String[] { tableName, colName, val});
	}
}
