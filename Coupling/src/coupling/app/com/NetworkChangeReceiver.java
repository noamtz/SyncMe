package coupling.app.com;

import coupling.app.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NetworkChangeReceiver extends BroadcastReceiver {
	 
    @Override
    public void onReceive(final Context context, final Intent intent) {
    	if(Utils.isNetworkAvailable()) {
    		Utils.Log("reciveir", "isNetworkAvailable" );
    		NetworkRequestQueue.getInstance().handleDBRequests();
    	}
    }

}