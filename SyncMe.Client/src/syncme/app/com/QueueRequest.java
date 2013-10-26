package syncme.app.com;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import syncme.app.data.Request;

public class QueueRequest {
	//private ConcurrentLinkedQueue<Request> queue;
	private Map<UUID,Request> queue;
	private static QueueRequest queueRequest;
	
	private QueueRequest(){
		queue = new HashMap<UUID, Request>();//new ConcurrentLinkedQueue<Request>();
	}
	
	public static QueueRequest getInstance(){
		if(queueRequest == null){
			queueRequest = new QueueRequest();
		}
		return queueRequest;
	}
	
	public void insertRequest(Request req){
		req.setId(UUID.randomUUID());
		queue.put(req.getId(),req);
	}
	
	public Request removeRequest(UUID reqId){
		return queue.remove(reqId);
	}
	
}
