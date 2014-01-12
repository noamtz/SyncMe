package coupling.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import coupling.app.App;

public class DALGroceryList {
	private DBHandler dbHandler;
	
	public DALGroceryList(){
		dbHandler = new DBHandler(App.getAppCtx());
	}
	
	public Cursor getSource(){
		return dbHandler.getReadableDatabase().rawQuery("SELECT * FROM GroceryList", null);
	}
	
	public long addItem(String item){
		ContentValues values = new ContentValues();
		values.put("ItemName", item);
		return dbHandler.getWritableDatabase().insertOrThrow("ShopList", null, values);		
	}
}
