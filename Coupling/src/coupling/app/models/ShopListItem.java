package coupling.app.models;


import java.util.HashMap;
import java.util.Map;

import coupling.app.Ids;
import coupling.app.Utils;
import static coupling.app.data.Constants.*;
import android.content.ContentValues;
import android.database.Cursor;

public class ShopListItem extends BaseModel{
	
	private String name;
	private Integer quantity;
	private Boolean isDone;
	private Long listId;
	
	public ShopListItem(Cursor c){
		ids = new Ids(c);
		listId =  c.getLong(c.getColumnIndexOrThrow(SHOPLIST_ID));
		name =  c.getString(c.getColumnIndexOrThrow(ITEM_NAME));
		quantity = c.getInt(c.getColumnIndexOrThrow(ITEM_QUANTITY));
		isDone = c.getInt(c.getColumnIndexOrThrow(IS_DONE)) > 0;
		isMine = c.getInt(c.getColumnIndexOrThrow(IS_MINE)) == 1;	
	}
	
	public ShopListItem(Long listId){
		this.listId = listId;
	}
	
	public ShopListItem(Long listId , Ids ids){
		this.listId = listId;
		this.ids = ids;
	}
	
	public ShopListItem(Long listId, String name, Integer quantity){
		this.name = name;
		this.quantity = quantity;
		this.listId = listId;
	}
	
	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean isDone() {
		return isDone;
	}

	public void setIsDone(Boolean isDone) {
		this.isDone = isDone;
	}

	@Override
	public ContentValues toDb() {
		ContentValues values = new ContentValues();
		if(listId != null)
			values.put(SHOPLIST_ID, listId);
		if(name != null)
			values.put(ITEM_NAME, name);
		if(quantity != null)
			values.put(ITEM_QUANTITY, quantity);
		if(ids.getGlobalId() != null)
			values.put(UID, ids.getGlobalId());
		if(isDone != null)
			values.put(IS_DONE, isDone);
		if(isMine != null)
			values.put(IS_MINE, isMine);
		return values;
	}

	@Override
	public Map<String, Object> toNetwork() {
		Map<String,Object> data = new HashMap<String, Object>();

		data.put(UID, getGlobalId());
		if(ids.getDBId() != null)
			data.put(LOCALID, ids.getDBId());
		if(name != null)
			data.put(ITEM_NAME, name);
		if(quantity != null)
			data.put(ITEM_QUANTITY, quantity);
		if(isDone != null)
			data.put(IS_DONE, isDone);
		return data;
	}
	
	@Override
	public String toString(){
		return ids + ", Name: " + " Quantity: " + quantity + ", IsDone: " + isDone + ", IsMine: " + isMine; 
	}
	
}
