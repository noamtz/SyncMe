package syncme.app.utils;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import syncme.app.App;


public class CommonUtils {
	public static void Log(String className , String msg){
		android.util.Log.v(className, className);
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

}
