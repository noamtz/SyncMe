package coupling.app.models;

import static coupling.app.data.Constants.IS_LOCKED;
import static coupling.app.data.Constants.IS_MINE;
import static coupling.app.data.Constants.LOCALID;
import static coupling.app.data.Constants.UID;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import coupling.app.Ids;

public abstract class BaseModel {

	protected Ids ids;
	protected Boolean isMine;
	protected Boolean isLocked;
	
	public Boolean isLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Ids getIds() {
		return ids;
	}

	public void setIds(Ids ids) {
		this.ids = ids;
	}

	public Boolean isMine() {
		return isMine;
	}

	public void setIsMine(Boolean isMine) {
		this.isMine = isMine;
	}
	
	public Long getGlobalId(){
		return ids.getGlobalId();
	}
	
	public Long getLocalId(){
		return ids.getDBId();
	}

	public BaseModel(){
		ids = new Ids();
	}
	
	public ContentValues toDb(){
		ContentValues values = new ContentValues();
		if(ids.getGlobalId() != null)
			values.put(UID, ids.getGlobalId());
		if(isMine != null)
			values.put(IS_MINE, isMine);
		if(isLocked != null)
			values.put(IS_LOCKED, isLocked);
		return values;
	}
	
	public Map<String,Object> toNetwork(){
		Map<String,Object> data = new HashMap<String, Object>();

		data.put(UID, getGlobalId());
		if(ids.getDBId() != null)
			data.put(LOCALID, ids.getDBId());
		return data;
	}
}
