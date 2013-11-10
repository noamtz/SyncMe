package syncme.app.model.shoplist;

import java.util.Date;

import syncme.app.utils.CommonUtils;
import android.content.ContentValues;
import android.database.Cursor;

import static syncme.app.data.DBConstants.*;

public class ShopListOverview {

	private long id;
	private String title;
	private Date createdAt;
	
	public ShopListOverview(){
	}
	
	public ShopListOverview(Cursor cursor){
	
		this.id = cursor.getLong(0);
		this.title = cursor.getString(1);
		this.createdAt = CommonUtils.toDate(cursor.getString(2));
	}
	
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
	public void setCreatedAt(String createdAtTemplate) {
		this.createdAt = CommonUtils.toDate(createdAtTemplate);
	}
	
	public ContentValues toDB(){
		ContentValues values = new ContentValues();
		values.put(SHOPLIST_OVERVIEW_TITLE, title);		
		return values;
	}
}
