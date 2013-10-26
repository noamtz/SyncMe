package syncme.app.com;

import syncme.app.data.Request;


public interface ITask {

	public void onTaskComplete(Request request, String response);
}
