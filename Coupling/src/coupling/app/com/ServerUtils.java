package coupling.app.com;

import java.util.ArrayList;

import coupling.app.Utils;

public class ServerUtils {

	public static String SERVER_URL = "http://10.8.4.176:8889/syncMeApp.php";
	public static String POST = "post";

	public static void post(Request request ,ArrayList<ITask> tasker){
		Utils.Log("ServerUtils", "<execute>");
		request.setServerIP(SERVER_URL);
		Utils.Log("ServerUtils", "execute: " + request.getMethod());
		new DoPOST(request, tasker).execute(null,null,null);
		Utils.Log("ServerUtils", "<execute>");
	}
}

