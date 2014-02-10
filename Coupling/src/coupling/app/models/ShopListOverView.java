package coupling.app.models;

import java.util.Date;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import coupling.app.Ids;
import coupling.app.Utils;
import static coupling.app.data.Constants.*;

public class ShopListOverView extends BaseModel {
	
	private String title;
	private Integer totalItems;
	private Date createdAt;

	public ShopListOverView(){}
	
	public ShopListOverView(Cursor c){
		ids = new Ids(c);
		title =  c.getString(c.getColumnIndexOrThrow(TITLE));
		totalItems = c.getInt(c.getColumnIndexOrThrow("TotalItems"));
		isMine = c.getInt(c.getColumnIndexOrThrow(IS_MINE)) == 1;	
		createdAt = Utils.toDate(c.getString(c.getColumnIndexOrThrow(CREATED_AT)));
		isLocked = c.getInt(c.getColumnIndexOrThrow(IS_LOCKED)) == 1;	
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public ContentValues toDb() {
		ContentValues values = super.toDb();
		if(title != null)
			values.put(TITLE, title);
		if(totalItems != null)
			values.put(TOTAL_ITEMS, totalItems);
		return values;
	}

	@Override
	public Map<String, Object> toNetwork() {
		Map<String,Object> data = super.toNetwork();
		if(title != null)
			data.put(TITLE, title);
		if(totalItems != null)
			data.put(TOTAL_ITEMS, totalItems);
		return data;
	}
}
