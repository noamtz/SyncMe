package syncme.app.logic;

public class CommonUtils {
	public static void Log(String className , String msg){
		android.util.Log.v(className, className);
	}
	
	public static void LogError(String className , String msg){
		android.util.Log.e(className, className);
	}
}
	