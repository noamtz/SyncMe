package syncme.app.com;

import java.util.UUID;


import syncme.app.data.Request;

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
	 * Excecute request to server via async task 
	 * @param request
	 * @author Noam Tzumie
	 */
	public void executeTask(Request request){
		//TODO check if there is no double request or some error.
		queueRequest.insertRequest(request);
		new DoPOST(request, this).execute(null,null,null);
	}

	/**
	 * This method is callback from DoPOST
	 * @author Noam Tzumie
	 */
	@Override
	public void onTaskComplete(UUID requestId, String response) {
		queueRequest.removeRequest(requestId);
	}
	
}
