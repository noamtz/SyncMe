package app.coupling;


import android.app.Application;
import android.content.Context;

public class App extends Application {

	private static Context appCtx;

	@Override
	public void onCreate() {
		super.onCreate();
		appCtx = getApplicationContext();
	}

	public static Context getAppCtx(){
		return appCtx;
	}

}
