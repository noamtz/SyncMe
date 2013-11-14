package syncme.app.model.shoplist;

import java.util.Date;

import syncme.app.utils.CommonUtils;
import android.content.ContentValues;
import android.database.Cursor;

import static syncme.app.data.DBConstants.*;

public class ShopListOverview {

	private long id;
	private String title;
	private int totalItems;
	
	
	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	private Date createdAt;

	public ShopListOverview(){
	}
	
	public ShopListOverview(Cursor c){
		id = c.getLong(c.getColumnIndex(SHOPLIST_OVERVIEW_ID));
		title = c.getString(c.getColumnIndex(SHOPLIST_OVERVIEW_TITLE));
		setCreatedAt(c.getString(c.getColumnIndex(SHOPLIST_OVERVIEW_CREATED_AT)));
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
		values.put(SHOPLIST_OVERVIEW_TOTALITEMS, totalItems);	
		return values;
	}


}
