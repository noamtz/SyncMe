package syncme.app;


import syncme.app.data.DAL;
import syncme.app.model.shoplist.Category;
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
		Category c  = new Category();
		if(DAL.getInstance().getCategories().getAllRecords().getCount() == 0){
			c.setName("fruit");
			DAL.getInstance().getCategories().create(c.toDB());
		}
	}

	public static Context getAppCtx(){
		return appCtx;
	}

}
