package syncme.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import syncme.app.App;


public class CommonUtils {
	public static void Log(String className , String msg){
		android.util.Log.v(className, msg);
	}
	
	public static void Log(String className , String methodName , String msg){
		android.util.Log.v(className, methodName + ": " + msg);
	}
	
	public static void LogError(String className , String methodName , String msg){
		android.util.Log.e(className, methodName + ": " + msg);
	}

	public static void LogError(String className , String msg){
		android.util.Log.e(className, className);
	}

	public static String getStringFromAssetFile(String relativePath){
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();

		try { 
			reader  = new BufferedReader(new InputStreamReader(App.getAppCtx().getAssets().open(relativePath)));
			sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static Date toDate(String template){
		Date date = null;
		try {
			date = SimpleDateFormat.getDateInstance().parse(template);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	
	public static String getResourceString(int resId){
		return App.getAppCtx().getResources().getString(resId);
	}
}
