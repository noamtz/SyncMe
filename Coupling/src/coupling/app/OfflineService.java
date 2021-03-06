package coupling.app;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import coupling.app.BL.BLNetworkOffline;
import coupling.app.com.NetworkRequestQueue;
import coupling.app.data.DALNetworkQueue;
import coupling.app.models.NetworkOfflineItem;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * This service takes all the requests that launch
 * when network was not available and send them synchronously
 * @author Noam Tzumie
 *
 */
public class OfflineService extends IntentService{

	private static final String TAG = "OfflineService";
	
	public OfflineService() {
		super("OfflineService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		NetworkRequestQueue network = NetworkRequestQueue.getInstance();
		BLNetworkOffline blNetworkOffline = BLNetworkOffline.getInstance();
		Mediator mediator = Mediator.getInstance();
		
		ArrayList<NetworkOfflineItem> dbQueue = blNetworkOffline.getQueue();

		Utils.Log("OfflineService", "size of queue: " + dbQueue.size());
		
		if(dbQueue.size() > 0){
			//hold all the offline requests that successfully sent
			ArrayList<NetworkOfflineItem> removed = new ArrayList<NetworkOfflineItem>();
			for (NetworkOfflineItem item : dbQueue)
			{
				if(Utils.isNetworkAvailable()){
					blNetworkOffline.handleNetworkItem(item);
					Log.v(TAG, item.getJson().toString());
					JSONObject json = network.postFutureJson(item.getJson());
					Log.v(TAG + " response", json.toString());
					
				
					mediator.manage(json,true);
					removed.add(item);
				}
			}
			for(NetworkOfflineItem i : removed)
				blNetworkOffline.remove(i);
		}
	}


}
