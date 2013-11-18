package syncme.app.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import syncme.app.model.shoplist.Category;
import syncme.app.model.shoplist.Item;
import syncme.app.model.shoplist.ShopList;
import syncme.app.model.shoplist.ShopListOverview;
import syncme.app.utils.CommonUtils;
import static syncme.app.data.DBConstants.*;

public class DataSourceShopList extends DataSource{
	
	private static final String SHOPLIST_OVERVIEW_ID = "shopListOverviewId";
	private static final String ITEM_ID = "itemId";
	private static final String QUANTITY = "quantity";
	private static final String DONE = "done";
	private static final String ITEM_NAME = "itemName";
	private static final String CATEGORY_ID = "categoryId";
	private static final String CATEGORY_NAME = "categoryName";
	
	private static final String TAG = "DataSourceShopList";
	
	private DataSource shopList;
	
	public DataSourceShopList(DBHandler dbHelper) {
		super(dbHelper, DBConstants.VIEW_SHOP_LIST);
		shopList = new DataSource(dbHelper,DBConstants.TABLE_SHOP_LIST);
	}
	
	/**
	 * 
	 * @param id
	 * @return null if the shoplist is not exist
	 */
	public ShopList getShopList(long id){
	
		Cursor c = getRecord(SHOPLIST_OVERVIEW_ID, Long.toString(id));
		
		ShopList shopList = new ShopList();
		shopList.setOverview(getShopListOverview(id));
		
		ArrayList<Item> items = new ArrayList<Item>();
		while(c.moveToNext()){
			Item item = new Item();
			item.setId(c.getLong(c.getColumnIndex(ITEM_ID)));
			item.setName(c.getString(c.getColumnIndex(ITEM_NAME)));
			
			Category category = new Category(); 
			category.setId(c.getLong(c.getColumnIndex(CATEGORY_ID)));
			category.setName(c.getString(c.getColumnIndex(CATEGORY_NAME)));
			
			item.setCategory(category);
			items.add(item);
		}
		
		shopList.setItems(items);
		return shopList;
	}

	/**
	 * 
	 * @param id
	 * @return null if the shoplistoverview not exist
	 */
	public ShopListOverview getShopListOverview(long id){
		Cursor coverview = DAL.getInstance().getShopListOverview().getRecord(id);
		if(coverview.getCount() == 0)
			return null;
		return new ShopListOverview(coverview);
	}

	/**
	 * 
	 * @return empty list if there is no shoplists
	 */
	public ArrayList<ShopListOverview> getAllShopListOverview(){
		Cursor coverview = DAL.getInstance().getShopListOverview().getAllRecords();
		ArrayList<ShopListOverview> lists = new ArrayList<ShopListOverview>();

		while(coverview.moveToNext()){
			lists.add(new ShopListOverview(coverview));
		}

		return lists;
	}
	
	public boolean createItem(long listId, Item item){
		//TODO: check if the list exist
		//TODO: check if returns always -1
		return shopList.create(itemToDB(listId, item)) != -1;
	}
	
	public boolean updateItem(long listId, Item item){
		boolean isUpdated = shopList.update(itemToDB(listId, item), 
				SHOPLIST_OVERVIEW_ID + " = " + listId + " AND " + ITEM_ID + " = " + item.getId(), null);
		CommonUtils.Log(TAG, "updateItem", "is succeeded: " + isUpdated);
		return isUpdated;
	}
	
	public boolean deleteItem(long listId, long itemId){
		int num = database.delete(TABLE_SHOP_LIST,SHOPLIST_OVERVIEW_ID + " = " + listId + " AND " + ITEM_ID + " = " + itemId, null);
		CommonUtils.Log(TAG, "delete" , TABLE_SHOP_LIST + " is succeeded: " + (num > 0));
		return num > 0;
	
	}
	
	private ContentValues itemToDB(long listId, Item item){
		ContentValues values = new ContentValues();
		values.put(SHOPLIST_OVERVIEW_ID, listId);
		values.put(ITEM_ID, item.getId());
		values.put(DONE, item.isDone());
		values.put(QUANTITY, item.getQuantity());
		return values;
	}
}
