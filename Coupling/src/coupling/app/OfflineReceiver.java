package coupling.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OfflineReceiver extends BroadcastReceiver {
	 
    @Override
    public void onReceive(final Context context, final Intent intent) {
    	if(Utils.isNetworkAvailable()) {
    		Utils.Log("NetworkChangeReceiver", "Online");
    		Intent myIntent=new Intent(context,OfflineService.class);        
    		context.startService(myIntent);
    	}
    }

}