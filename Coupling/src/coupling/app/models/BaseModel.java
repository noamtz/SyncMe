package coupling.app.models;

import java.util.Map;

import android.content.ContentValues;
import coupling.app.Ids;

public abstract class BaseModel {

	protected Ids ids;
	protected Boolean isMine;
	
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
	
	public abstract ContentValues toDb();
	
	public abstract Map<String,Object> toNetwork();
}
