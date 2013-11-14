package syncme.app.model.shoplist;

import android.content.ContentValues;

public class Item {

	private static final String ITEM_ID = "id";
	private static final String ITEM_NAME = "name";
	private static final String ITEM_CATEGORY_ID= "categoryId";
	
	private long id;
	private String name;
	private Category category;
	//Belong to shopList
	private int quantity;
	private boolean done;
	
	public Item(){
		done = false;
	}
	
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
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
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public ContentValues toDB(){
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, id);
		values.put(ITEM_NAME, name);
		values.put(ITEM_CATEGORY_ID, category.getId());
		return values;
	}
	
}
