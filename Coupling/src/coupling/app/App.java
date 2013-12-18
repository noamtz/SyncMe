package coupling.app;


import coupling.app.com.Constants;
import coupling.app.com.User;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {

	private static Context appCtx;
	private static User owner;
	
	@Override
	public void onCreate() {
		super.onCreate();
		appCtx = getApplicationContext();
	}

	public static Context getAppCtx(){
		return appCtx;
	}
	
	public static void loadOwner(){
		SharedPreferences prefs = Utils.getSPF();
		if(prefs.getBoolean(Constants.IS_REGISTERED, false)){
			owner = new User();
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
