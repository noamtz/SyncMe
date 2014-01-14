package coupling.app;

public class CalenderEvent {
	
	private String title;
	private String description;
	private String startTime;
	private String endTime;
	
	public CalenderEvent(String title, String description, String startTime, String endTime) {
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getstartTime() {
		return startTime;
	}
	public void setstartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getendTime() {
		return endTime;
	}
	public void setendTime(String endTime) {
		this.endTime = endTime;
	}
	
}
