package syncme.app;


import syncme.app.utils.CommonUtils;
import android.app.Application;
import android.content.Context;

public class App extends Application {

	private static Context appCtx;

	private static final String TAG = "App";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appCtx = getApplicationContext();
		CommonUtils.Log(TAG, "onCreate", appCtx.getPackageName());
		//TODO: DEBUG
		CommonUtils.copyDB();
	}

	public static Context getAppCtx(){
		return appCtx;
	}

}
