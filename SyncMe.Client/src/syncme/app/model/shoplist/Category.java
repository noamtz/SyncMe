package syncme.app.model.shoplist;

import android.content.ContentValues;

public class Category {

	private static final String CATEGORY_ID = "_id";
	private static final String CATEGORY_NAME = "name";
	
	private long id;
	private String name;
	
	public Category(){
		setId(1);
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
	
	public ContentValues toDB(){
		ContentValues values = new ContentValues();
		values.put(CATEGORY_NAME, name);
		return values;
	}
}
