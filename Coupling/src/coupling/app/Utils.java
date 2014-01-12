package coupling.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
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
	
	public static void shopToast(String msg){
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
}
