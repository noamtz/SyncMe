package syncme.app.data;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import syncme.app.model.shoplist.Category;
import syncme.app.model.shoplist.Item;
import syncme.app.model.shoplist.ShopList;
import syncme.app.model.shoplist.ShopListOverview;
import static syncme.app.data.DBConstants.*;

public class DataSourceShopList extends DataSource{
	
	private static final String SHOPLIST_OVERVIEW_ID = "shopListOverviewId";
	private static final String ITEM_ID = "itemId";
	private static final String QUANTITY = "quantity";
	private static final String DONE = "done";
	private static final String ITEM_NAME = "itemName";
	private static final String CATEGORY_ID = "categoryId";
	private static final String CATEGORY_NAME = "categoryName";
	
	
	private DataSource shopList;
	
	public DataSourceShopList(DBHandler dbHelper) {
		super(dbHelper, DBConstants.VIEW_SHOP_LIST);
		shopList = new DataSource(dbHelper,DBConstants.TABLE_SHOP_LIST);
	}
	
	public ShopList getShopList(long id){
		//TODO: check if the list exist
		Cursor coverview = DAL.getInstance().getShopListOverview().getRecord(id);
		Cursor c = getRecord(id);
		
		ShopList shopList = new ShopList();
		ShopListOverview shopListOverview  = new ShopListOverview();
		
		String title = coverview.getString(c.getColumnIndex(SHOPLIST_OVERVIEW_TITLE));
		String createdAt = coverview.getString(c.getColumnIndex(SHOPLIST_OVERVIEW_CREATED_AT));
		
		shopListOverview.setId(id);
		shopListOverview.setTitle(title);
		shopListOverview.setCreatedAt(createdAt);
		
		shopList.setOverview(shopListOverview);
		
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
	
	public boolean createItem(long listId, Item item){
		//TODO: check if the list exist
		//TODO: check if returns always -1
		return shopList.create(itemToDB(listId, item)) != -1;
	}
	
	public boolean updateItem(long listId, Item item){
		return shopList.update(itemToDB(listId, item), 
				SHOPLIST_OVERVIEW_ID + "=?" + " AND " + ITEM_ID + "=?", new String[]{ listId +"", item.getId() +""});
	}
	
	public void deleteItem(long listId, long itemId){
		//TODO: handle delete exceptions.
		database.delete(TABLE_SHOP_LIST, SHOPLIST_OVERVIEW_ID + "=?" + " AND " + ITEM_ID + "=?", new String[]{ listId +"", itemId +""});
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
