package coupling.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.nit.coupling.R;

import coupling.app.data.Constants;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class Utils {

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
		android.util.Log.i(className, msg);
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

	public static void showToast(String msg){
		Toast.makeText(App.getAppCtx(), msg, Toast.LENGTH_LONG).show();
	}

	public static String removeHtml(String target){
		return target.replaceAll("<(.|\n)*?>", "").trim();
	}

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

	public static void sendNotification(String msg , Class<?> target, JSONObject data) {
		NotificationManager mNotificationManager = (NotificationManager)
				App.getAppCtx().getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notifIntent = new Intent(App.getAppCtx(), target);
		if(data.has(Constants.LOCAL_LIST_ID))
			try {
				notifIntent.putExtra(Constants.LOCAL_LIST_ID, data.getString(Constants.LOCAL_LIST_ID));
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
		.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(Constants.NOTIFICATION_ID, mBuilder.build());
	}

	
	//public static ArrayList<CalenderEvent> events = new ArrayList<CalenderEvent>();
	//public static ArrayList<String> nameOfEvent = new ArrayList<String>();
	//public static ArrayList<String> startDates = new ArrayList<String>();
	//public static ArrayList<String> endDates = new ArrayList<String>();
	//public static ArrayList<String> descriptions = new ArrayList<String>();
	/*
	public static ArrayList<String> readCalendarEvent(Context context) {
		Cursor cursor = context.getContentResolver()
				.query(Uri.parse("content://com.android.calendar/events"),
						new String[] { "calendar_id", "title", "description",
					"dtstart", "dtend", "eventLocation" }, null,
					null, null);
		cursor.moveToFirst();
		// fetching calendars name
		String CNames[] = new String[cursor.getCount()];

		// fetching calendars id
		nameOfEvent.clear();
		startDates.clear();
		endDates.clear();
		descriptions.clear();
		Log.w("DEBUG","length: " + CNames.length);
		/*for (int i = 0; i <0; i++) {

			nameOfEvent.add(cursor.getString(1));
			startDates.add(getDate(Long.parseLong(cursor.getString(3))));
			//Log.w("DEBUG", i +", " + cursor.getString(4));
			//endDates.add(getDate(Long.parseLong(cursor.getString(4))));
			descriptions.add(cursor.getString(2));
			CNames[i] = cursor.getString(1);
			cursor.moveToNext();

		}
		Calendar cal = Calendar.getInstance();
		nameOfEvent.add("HELP1");//cal
		startDates.add(convertMillisToSimpleDate(cal.getTimeInMillis() - (60*60*1000*2)));
		endDates.add(convertMillisToSimpleDate(cal.getTimeInMillis() - (60*60*1000*3)));
		descriptions.add("IN DEBUG MODE1");

		nameOfEvent.add("HELP2");//cal
		startDates.add(convertMillisToSimpleDate(cal.getTimeInMillis() - (60*60*1000*5)));
		endDates.add(convertMillisToSimpleDate(cal.getTimeInMillis() - (60*60*1000*4)));
		descriptions.add("IN DEBUG MODE2");

		return nameOfEvent;
	}*/

}
