package coupling.app;

import java.io.IOException;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParserException;

import coupling.app.BL.BLFactory;
import coupling.app.BL.BLGroceryList;
import coupling.app.data.XMLParser;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

public class GroceryList {
	private BLGroceryList blGroceryList;
	private LinkedList<String> GroceryList;
	
	public GroceryList(){
		GroceryList = new LinkedList<String>();
		blGroceryList = BLFactory.getInstance().getGroceryList();

	}
	
	public void loadItems(){
		Cursor c = blGroceryList.getSource();
		if (c.moveToFirst()) {
	        do {
	        	//Utils.Log("GroceryList", "C-tor", c.getString(1));
	        	GroceryList.add(c.getString(c.getColumnIndex("ItemName")));
	        } while (c.moveToNext());
		}
        c.close();
	}
	
	public void addItem(String item, ArrayAdapter<String> adapter) {
		if (!GroceryList.contains(item)) {
			addItem(item);
			adapter.add(item);
		}
	}
	
	public void addItem(String item){
		blGroceryList.addItem(item);
	}
	
	public LinkedList<String> getGroceryList(){
		if (GroceryList == null)
			GroceryList = new LinkedList<String>();
		return GroceryList;
	}
	
	public void  initGroceryListItems(Activity a){
		Log.w("ILAN DEBUG", "INIT");
		XMLParser xml = new XMLParser(a);
		
		try {
			LinkedList<String> list = xml.parseItems();
			for (String item : list) {
				addItem(item);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
