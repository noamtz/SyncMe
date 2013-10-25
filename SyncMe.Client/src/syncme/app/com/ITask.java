package syncme.app.com;

import java.util.UUID;


public interface ITask {

	public void onTaskComplete(UUID requestId, String response);
}
