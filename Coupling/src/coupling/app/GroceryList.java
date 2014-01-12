package coupling.app;

import java.util.LinkedList;

import coupling.app.BL.BLFactory;
import coupling.app.BL.BLGroceryList;

import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

public class GroceryList {
	private BLGroceryList blGroceryList;
	private LinkedList<String> GroceryList;
	
	public GroceryList(){
		GroceryList = new LinkedList<String>();
		blGroceryList = BLFactory.getInstance().getGroceryList();
		Cursor c = blGroceryList.getSource();
		Log.w("GL", c.getColumnCount() + "");
		if (c.moveToFirst()) {
	        do {
	        	GroceryList.add(c.getString(0));
	        	Log.w("GL", "added item from cursor: " + c.getString(0));
	        } while (c.moveToNext());
		}
        c.close();
	}
	
	public void addItem(String item, ArrayAdapter<String> adapter) {
		if (!GroceryList.contains(item)) {
			boolean flag = blGroceryList.addItem(item);
			Log.w("GL", "added item: " + item + ", flag: " + flag);
			adapter.add(item);
		}
	}
	
	public LinkedList<String> getGroceryList(){
		if (GroceryList == null)
			GroceryList = new LinkedList<String>();
		return GroceryList;
	}
}
