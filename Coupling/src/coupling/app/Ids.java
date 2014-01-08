package coupling.app;

public class Ids {

	private long DBId;
	private Long globalId;
	
	public Ids(){
	}
	
	public Ids(long DBId, Long globalId){
		this.DBId = DBId;
		this.globalId = globalId;
	}

	public long getDBId() {
		return DBId;
	}

	public void setDBId(long dBId) {
		DBId = dBId;
	}

	public Long getGlobalId() {
		return globalId;
	}

	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}	
	
	public String toString(){
		return "DB Id: " + DBId + " , Global Id: " + globalId;
	}
}
