package syncme.app.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;






import syncme.app.model.Request;
import syncme.app.utils.CommonUtils;
import android.os.AsyncTask;
import android.util.Log;
import static syncme.app.logic.Constants.*;

public class ServerUtils {

	public static String SERVER_URL = "http://10.0.0.1/SyncMe/SyncMe.Server/syncMeApp.php";
	public static String POST = "post";

	private static ComManager comManager = ComManager.getInstance(); 


	public static void execute(Request request ,ITask tasker){
		CommonUtils.Log("ServerUtils", "<execute>");
		request.setServerIP(SERVER_URL);
		CommonUtils.Log("ServerUtils", "execute: " + request.getMethod());
		comManager.executeTask(request, tasker);
		CommonUtils.Log("ServerUtils", "<execute>");
	}
}

