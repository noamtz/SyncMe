package coupling.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.nit.coupling.R;

import coupling.app.data.Constants;

public class Utils {

	private static final boolean DEBUG = true;
	
	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static Date toDate(String template){
		Date date = null;
		try {
			SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = inFormat.parse(template);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static void Log(String className , String msg){
		if(DEBUG)
		android.util.Log.i(className, msg);
	}

	public static void Log(String className , String methodName , String msg){
		if(DEBUG)
		android.util.Log.v(className, "<"+methodName+">" + ": " + msg);
	}

	public static void LogError(String className , String methodName , String msg){
		android.util.Log.e(className,  "<"+methodName+">" +  ": " + msg);
	}

	public static void LogError(String className , String msg){
		android.util.Log.e(className, msg);
	}

	public static void showToast(String msg){
		Toast.makeText(App.getAppCtx(), msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * The free php server respond with hidden html tags
	 * so this method removes them
	 * @param target
	 * @return
	 */
	public static String removeHtml(String target){
		return target.replaceAll("<(.|\n)*?>", "").trim();
	}

	/**
	 * Debug purposes
	 * @param databaseName
	 */
	public static void exportDatabse(String databaseName) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//"+App.getAppCtx().getPackageName()+"//databases//"+databaseName+"";
				String backupDBPath = "backupname.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {

		}
	}

	
	public static long convertSimpleDateToMillis(String simpleDate){

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setLenient(false);

		Date date = null;
		try {
			date = formatter.parse(simpleDate);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}


	public static String convertMillisToSimpleDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	/**
	 * Sending Notification when new item is come in
	 * @param msg
	 * @param target
	 * @param data
	 */
	public static void sendNotification(String msg , Class<?> target, JSONObject data) {
		NotificationManager mNotificationManager = (NotificationManager)
				App.getAppCtx().getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent notifIntent = new Intent(App.getAppCtx(), target);
		//Move to appropriate shoplist when notification is clicked
		if(data.has(Constants.LOCAL_LIST_ID))
			try {
				Bundle bundle = new Bundle();
				bundle.putLong(Constants.LOCAL_LIST_ID, data.getLong(Constants.LOCAL_LIST_ID));
				notifIntent.putExtras(bundle);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		//For putExtra to work
		notifIntent.setAction("Couling");
		
		PendingIntent contentIntent = PendingIntent.getActivity(App.getAppCtx(), 0,
				notifIntent, 0);
		
		
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(App.getAppCtx())
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(Constants.NOTIF_TITLE)
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(msg))
		.setAutoCancel(true)
		.setContentText(msg);
		
		
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(Constants.NOTIFICATION_ID, mBuilder.build());
	}

	
	public static Long parseToLong(Object num){
		Long res = null;
		if(num != null)
			if(num instanceof String)
				try { res = Long.parseLong((String)num); }
				catch(NumberFormatException e){}
			else if(num instanceof Long)
				res = (Long) num;
		Log("UTILS", "LISTID: " + res);
		return res;
	}
	
	public static boolean isStringANumber(String num){
		try{
			Integer.parseInt(num);
			return true;
		} catch(NumberFormatException e){
			return false;
		}
	}
	/**
	 * Check if network is available
	 * @return
	 */
	public static boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) App.getAppCtx().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
