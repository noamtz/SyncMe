package syncme.app.com;


import java.util.ArrayList;

import syncme.app.model.Request;
import syncme.app.utils.CommonUtils;

public class ComManager implements ITask{

	private QueueRequest queueRequest = QueueRequest.getInstance();
	
	private static ComManager comManager;
	
	private ComManager(){
	}
	
	public static ComManager getInstance(){
		if(comManager == null)
			comManager = new ComManager();
		return comManager;
	}
	
	/**
	 * Execute request to server via async task 
	 * @param request
	 * @author Noam Tzumie
	 */
	public void executeTask(Request request, ITask tasker){
		//TODO check if there is no double request or some error.
		queueRequest.insertRequest(request);
		ArrayList<ITask> taskManagers = new ArrayList<ITask>();
		taskManagers.add(this);
		taskManagers.add(tasker);
		new DoPOST(request, taskManagers).execute(null,null,null);
	}

	/**
	 * This method is callback from DoPOST
	 * @author Noam Tzumie
	 */
	@Override
	public void onTaskComplete(Request request, String response) {
		CommonUtils.Log("ComManager", "<onTaskComplete>");
		queueRequest.removeRequest(request.getId());
		CommonUtils.Log("ComManager", "</onTaskComplete>");
	}
	
}
