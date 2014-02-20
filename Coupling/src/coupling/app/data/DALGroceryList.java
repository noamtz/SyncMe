package coupling.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import coupling.app.App;

public class DALGroceryList {
	private DBHandler dbHandler;
	
	public DALGroceryList(){
		dbHandler = new DBHandler(App.getAppCtx());
	}
	
	//get all grovery list info from DB
	public Cursor getSource(){
		return dbHandler.getReadableDatabase().rawQuery("SELECT DISTINCT ItemName FROM GroceryList", null);
	}
	
	//add item to DB
	public long addItem(String item){
		ContentValues values = new ContentValues();
		values.put("ItemName", item);
		return dbHandler.getWritableDatabase().insertOrThrow("GroceryList", null, values);		
	}
}
