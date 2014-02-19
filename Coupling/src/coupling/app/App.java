package coupling.app;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import coupling.app.com.NetworkRequestQueue;
import coupling.app.data.Constants;
import coupling.app.models.User;

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
		Utils.Log("App", "Starting Coupling");
		NetworkRequestQueue.getInstance().handleDBRequests();
	}

	public static synchronized Context getAppCtx(){
		return appCtx;
	}

	public static synchronized App getInstance(){
		return app;
	}

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
