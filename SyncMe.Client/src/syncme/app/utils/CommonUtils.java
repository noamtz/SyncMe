package syncme.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import syncme.app.App;
import syncme.app.data.DBConstants;
import android.annotation.SuppressLint;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.method.DateTimeKeyListener;


public class CommonUtils {
	public static void Log(String className , String msg){
		android.util.Log.v(className, msg);
	}

	public static void Log(String className , String methodName , String msg){
		android.util.Log.v(className, "<"+methodName+">" + ": " + msg);
	}

	public static void LogError(String className , String methodName , String msg){
		android.util.Log.e(className,  "<"+methodName+">" +  ": " + msg);
	}

	public static void LogError(String className , String msg){
		android.util.Log.e(className, msg);
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
			SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = inFormat.parse(template);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}


	public static String getResourceString(int resId){
		return App.getAppCtx().getResources().getString(resId);
	}

	public static void copyDB(){
		File sd = Environment.getExternalStorageDirectory();

		if (sd.canWrite()) {

			File currentDB = App.getAppCtx().getDatabasePath(DBConstants.DATABASE_NAME);
			File backupDB = new File(sd, "backup.db");
			try{
				if (currentDB.exists()) {

					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					MediaScannerConnection.scanFile(App.getAppCtx(), new String[] {sd.getPath() + "/backup.db" }
					, new String[] {"text/plain"}
					, null);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<String> getTablesQuery(){
		ArrayList<String> result = new ArrayList<String>();
		String[] script = CommonUtils.getStringFromAssetFile(DBConstants.INIT_DB_SCRIPT_PATH).trim().split(" ");
		String table = "";
		for(int i=0; i<script.length; i++){
			String s = script[i];
			if(s.contentEquals("CREATE") && i > 0){
				result.add(table);
				table = "";
			}
			table += s + " ";
		}
		if(script.length > 0)
			result.add(table);
		return result;
	}
}
