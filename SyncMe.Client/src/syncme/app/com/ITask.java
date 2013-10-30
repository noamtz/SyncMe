package syncme.app.com;

import syncme.app.model.Request;


public interface ITask {

	public void onTaskComplete(Request request, String response);
}
