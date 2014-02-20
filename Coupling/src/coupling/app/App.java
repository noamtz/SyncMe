package coupling.app;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import coupling.app.com.NetworkRequestQueue;
import coupling.app.data.Constants;
import coupling.app.models.User;
/**
 * The Application class holds the User Session of
 * the application from the shared prefernces
 * and also starts the offline serive to sync to server
 * @author Noam Tzumie
 *
 */
public class App extends Application {
	
	private static Context appCtx;
	private static User owner;
	private static App app;


	@Override
	public void onCreate() {
		super.onCreate();

		app = this;

		appCtx = getApplicationContext();
		owner = new User();	
		Log.v("App", "Starting Coupling");
		
		Intent myIntent=new Intent(appCtx,OfflineService.class);        
		appCtx.startService(myIntent);
	}

	public static synchronized Context getAppCtx(){
		return appCtx;
	}

	public static synchronized App getInstance(){
		return app;
	}

	/**
	 * Load the owner user from shared preferences
	 * @param prefs
	 */
	public static void loadOwner(SharedPreferences prefs){
		Utils.Log("APP","USER REGISTERED?: " + prefs.getBoolean(Constants.IS_REGISTERED, false));

		if(prefs.getBoolean(Constants.IS_REGISTERED, false)){
			owner.setEmail(prefs.getString(Constants.EMAIL, null));
			owner.setFirstname(prefs.getString(Constants.FIRSTNAME, null));
			owner.setLastname(prefs.getString(Constants.LASTNAME, null));
			owner.setRegid(prefs.getString(Constants.REG_ID, null));
		}
	}

	public static void setOwner(User owr){
		owner = owr;
	}

	public static User getOwner(){
		return owner;
	}

}
