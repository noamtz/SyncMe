package coupling.app.models;

import android.database.Cursor;
import static coupling.app.data.Constants.*;

public class Ids {

	private Long DBId;
	private Long globalId;
	
	public Ids(){
		globalId = null;
	}

	public Ids(Cursor c){
		DBId = c.getLong(c.getColumnIndexOrThrow(ID));
		globalId = c.getLong(c.getColumnIndexOrThrow(UID));
	}

	public Ids(Long DBId, Long globalId){
		this.DBId = DBId;
		this.globalId = globalId;
	}

	public Long getDBId() {
		return DBId;
	}

	public void setDBId(long dBId) {
		DBId = dBId;
	}

	public Long getGlobalId() {
		if(globalId == null || globalId == 0)
			return null;
		return globalId;
	}

	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}	
	
	public String toString(){
		return "DB Id: " + DBId + " , Global Id: " + globalId;
	}
}
