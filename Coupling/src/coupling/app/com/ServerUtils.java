package coupling.app.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import coupling.app.Utils;
import coupling.app.data.Enums.HttpType;

public class ServerUtils {
	public final String TAG = this.getClass().getName();

	//public static String SERVER_URL = "http://10.0.0.2/SyncMe/SyncMe.Server/syncMeApp.php";
	public static String SERVER_URL = "http://coupling.herobo.com/api/syncMeApp.php";
	public static String POST = "post";

	private static ServerUtils serverUtils; 

	private ServerUtils(){
	}

	public static ServerUtils getInstance(){
		if(serverUtils == null)
			serverUtils = new ServerUtils();
		return serverUtils;
	}


	public JSONObject post(Request request ,ITask tasker , boolean async){
		ArrayList<ITask> taskers = new ArrayList<ITask>();
		taskers.add(tasker);
		return post(request, taskers, async);
	}
	
	/**
	 * 
	 * @param request
	 * @param tasker
	 * @param async
	 * @return
	 */
	public JSONObject post(Request request ,ArrayList<ITask> tasker , boolean async){
		Utils.Log(TAG, "<execute>");

		request.setServerIP(SERVER_URL);
		request.setHttpType(HttpType.POST);

		JSONObject resp = null;
		
		if(async){
			Utils.Log(TAG, request.toString());
			new RequestTask(request, tasker).execute(null,null,null);
		}
		else{
			resp = postRequest(request);
			if(resp != null){
				//Utils.Log(TAG, "post", resp.toString());
				//notifyTaskers(tasker, request, resp);
			} else {
				Utils.LogError(TAG, "Failed to execute post form request: " + request.toString());
			}
		}
		Utils.Log(TAG, "</execute>");
		return resp;
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
		
		String result = "";
		try {
			switch (request.getHttpType()) {
			case GET:
				response = httpclient.execute(new HttpGet(request.getServerIP()));
				break;

			case POST:
				HttpPost httppost = new HttpPost(request.getServerIP());

				String json = request.prepareToPost();
				if(json == null)
					return null;
				StringEntity se = new StringEntity(json,HTTP.UTF_8);
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "text/json; charset=utf-8"));
				
				httppost.setEntity(se);

				response = httpclient.execute(httppost);
				break;
			}

			entity = response.getEntity();
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				result = EntityUtils.toString(entity);
				result = Utils.removeHtml(result);
				// Create a JSON object from the request response
				Utils.Log("DEBUG", result);
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
			Utils.showToast(result);
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
		if(taskManagers != null){
			Response response =  new Response(result);
			for(ITask tasker : taskManagers){
				tasker.onTaskComplete(request ,response);
			}
			if(response.getMessageId() != null)
				API.getInstance().messageRecieved(response.getMessageId());
		}
	}
	
	public static String convertStreamToString( InputStream is, String ecoding ) throws IOException
	{
	    StringBuilder sb = new StringBuilder( Math.max( 16, is.available() ) );
	    char[] tmp = new char[ 4096 ];

	    try {
	       InputStreamReader reader = new InputStreamReader( is, ecoding );
	       for( int cnt; ( cnt = reader.read( tmp ) ) > 0; )
	            sb.append( tmp, 0, cnt );
	    } finally {
	        is.close();
	    }
	    return sb.toString();
	}
}

