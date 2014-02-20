package coupling.app;

import java.io.IOException;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParserException;

import coupling.app.BL.BLFactory;
import coupling.app.BL.BLGroceryList;
import coupling.app.com.API;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Activity for the grocery list for adding items with Autocomplete.
 * Give the ability for doing the following operations:
 * 1. add new item
 * 2. init grocery list to Db from XML file
 * 3. get Grocery List
 * @author ibental
 *
 */
public class GroceryList {
	private BLGroceryList blGroceryList;
	private LinkedList<String> groceryList;
	
	private static GroceryList gl;
	
	private GroceryList(){
		groceryList = new LinkedList<String>();
		blGroceryList = BLFactory.getInstance().getGroceryList();

	}
	public static GroceryList getInstance(){
		if(gl == null)
			gl = new GroceryList();
		return gl;
	}
	
	//loads all grocery list items from DB
	public void loadItems(){
		Cursor c = blGroceryList.getSource();
		if (c.moveToFirst()) {
	        do {
	        	if (!groceryList.contains(c.getString(c.getColumnIndex("ItemName"))))
	        		groceryList.add(c.getString(c.getColumnIndex("ItemName")));
	        } while (c.moveToNext());
		}
        c.close();
	}
	
	//add new item
	public void addItem(String item, ArrayAdapter<String> adapter) {
		if (!groceryList.contains(item)) {
			addItem(item);
			adapter.add(item);
		}
	}
	
	public void addItem(String item){
		blGroceryList.addItem(item);
	}
	
	//get the list
	public LinkedList<String> getGroceryList(){
		if (groceryList == null)
			groceryList = new LinkedList<String>();
		return groceryList;
	}
	
	//add to DB the init grocery list from the xml file
	public void  initGroceryListItems(){
		XMLParser xml = new XMLParser();
		
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
