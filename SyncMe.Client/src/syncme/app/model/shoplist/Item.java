package syncme.app.model.shoplist;

import android.content.ContentValues;

public class Item {

	private static final String ITEM_ID = "id";
	private static final String ITEM_NAME = "name";
	private static final String ITEM_CATEGORY_ID= "categoryId";
	
	private long id;
	private String name;
	private long categoryId;
	//Belong to shopList
	private int quantity;
	
	
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	
	public ContentValues toDB(){
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, id);
		values.put(ITEM_NAME, name);
		values.put(ITEM_CATEGORY_ID, categoryId);
		return values;
	}
	
}
