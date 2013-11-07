package syncme.app.model.shoplist;

import java.util.Date;

import android.content.ContentValues;

public class ShopListOverview {
	
	private static final String SHOPLIST_OVERVIEW_ID = "id";
	private static final String SHOPLIST_OVERVIEW_TITLE = "title";
	//TODO: check if 'created_at' automatic created in db
	private static final String SHOPLIST_OVERVIEW_CREATED_AT = "createdAt"; 
	
	private long id;
	private String title;
	private Date createdAt;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public ContentValues toDB(){
		ContentValues values = new ContentValues();
		values.put(SHOPLIST_OVERVIEW_TITLE, title);		
		return values;
	}
}
