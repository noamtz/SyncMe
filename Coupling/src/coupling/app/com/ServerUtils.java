package coupling.app.com;

import static coupling.app.com.Constants.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.JsPromptResult;
import coupling.app.Utils;
import coupling.app.data.Enums.HttpType;

public class ServerUtils {
	public final String TAG = this.getClass().getName();

	public static String SERVER_URL = "http://10.0.0.2/SyncMe/SyncMe.Server/syncMeApp.php";
	public static String POST = "post";

	private static ServerUtils serverUtils; 

	private ServerUtils(){
	}

	public static ServerUtils getInstance(){
		if(serverUtils == null)
			serverUtils = new ServerUtils();
		return serverUtils;
	}


	/**
	 * 
	 * @param request
	 * @param tasker
	 * @param async
	 * @return
	 */
	public void post(Request request ,ArrayList<ITask> tasker , boolean async){
		Utils.Log(TAG, "<execute>");

		request.setServerIP(SERVER_URL);
		request.setHttpType(HttpType.POST);

		if(async){
			Utils.Log(TAG, "execute unsynchronize: " + request.getMethod() + " TO: " + request.getServerIP());
			new RequestTask(request, tasker).execute(null,null,null);
		}
		else{
			Utils.Log(TAG, "execute synchronize: " + request.getMethod() + " TO: " + request.getServerIP());
			JSONObject resp = postRequest(request);
			Utils.Log(TAG, "post", resp.toString());
			notifyTaskers(tasker, request, resp);
		}
		Utils.Log(TAG, "</execute>");
	}

	private JSONObject postRequest(Request request){
		JSONObject serverResponse = null;

		//Create the HTTP request
		HttpParams httpParameters = new BasicHttpParams();

		//Setup timeouts
		HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
		HttpConnectionParams.setSoTimeout(httpParameters, 15000);	
		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		HttpResponse response = null;
		HttpEntity entity = null;

		try {
			switch (request.getHttpType()) {
			case GET:
				response = httpclient.execute(new HttpGet(request.getServerIP()));
				break;

			case POST:
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				// Add your parameters
				Log.v("API", request.getMethod());
				Log.v("API", request.getParams());
				nameValuePairs.add(new BasicNameValuePair(METHOD, request.getMethod()));
				nameValuePairs.add(new BasicNameValuePair(PARAMS, request.getParams()));

				HttpPost httppost = new HttpPost(request.getServerIP());
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
				break;
			}

			entity = response.getEntity();
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				String result = EntityUtils.toString(entity);
				Utils.Log("ERROR", result);
				// Create a JSON object from the request response
				serverResponse = new JSONObject(result);
			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}

		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return serverResponse;
	}


	private class RequestTask extends AsyncTask<String, String, JSONObject>{

		public final String TAG = this.getClass().getName();

		//Check if an exception has thrown
		Exception exception = null;

		ArrayList<ITask> taskManagers;

		Request request;

		public RequestTask(Request request, ArrayList<ITask> taskers){
			this.request = request;
			this.taskManagers = taskers;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//Check if Internet connection exist. 
		}

		@Override
		protected JSONObject doInBackground(String... msg) {
			return postRequest(request);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			//On UI thread
			Utils.Log(TAG, "<onPostExecute>");
			notifyTaskers(taskManagers, request, result);
			
			if(exception != null)
				Utils.LogError(TAG, exception.getMessage());
			Utils.Log("DoPost", "</onPostExecute>");
		}
	}

	private void notifyTaskers(ArrayList<ITask> taskManagers , Request request , JSONObject result){
		Utils.Log(this.getClass().getName(), "NOTIFY " + taskManagers);
		if(taskManagers != null){
			Response response =  new Response(result);
			for(ITask tasker : taskManagers){
				tasker.onTaskComplete(request ,response);
				Utils.Log(TAG, "Notify to :" + tasker.getClass().getName());
			}
			if(response.getMessageId() != null)
				API.getInstance().messageRecieved(response.getMessageId());
		}
	}
}

