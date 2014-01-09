package coupling.app;

public class Ids {

	private Long DBId;
	private Long globalId;
	private boolean remote;
	
	public Ids(){
		remote = false;
		globalId = null;
	}
	
	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
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
