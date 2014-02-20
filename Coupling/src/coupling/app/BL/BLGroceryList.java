package coupling.app.BL;

import android.database.Cursor;
import coupling.app.data.DALGroceryList;

public class BLGroceryList {
	private DALGroceryList dataSource;
	
	public BLGroceryList(){
		dataSource = new DALGroceryList();
	}
	
	//add new item to DB
	public boolean addItem(String item) {
		long id = dataSource.addItem(item);
		return (id != -1) ;
	}
	
	//get all info of grocery list
	public Cursor getSource(){
		return dataSource.getSource();
	}
}
