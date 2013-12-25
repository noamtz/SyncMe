package coupling.app;

public class Ids {

	private long DBId;
	private String globalId;
	
	public Ids(){
	}
	
	public Ids(long DBId, String globalId){
		this.DBId = DBId;
		this.globalId = globalId;
	}

	public long getDBId() {
		return DBId;
	}

	public void setDBId(long dBId) {
		DBId = dBId;
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}	
	
	public String toString(){
		return "DB Id: " + DBId + " , Global Id: " + globalId;
	}
}
