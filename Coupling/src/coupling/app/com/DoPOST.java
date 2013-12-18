package coupling.app.com;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;

import coupling.app.Utils;
import static coupling.app.com.Constants.*;

public class DoPOST extends AsyncTask<String, Void, Boolean>{

	public final String TAG = this.getClass().getName();
	
	//Check if an exception has thrown
	Exception exception = null;
	
	JSONObject serverResponse;
	
	ArrayList<ITask> taskManagers;
	Request req;
	
	public DoPOST(Request req , ArrayList<ITask> taskManagers){
		this.req = req;
		this.taskManagers = taskManagers;
	}
	
	public DoPOST(Request req , ITask tasker){
		this.req = req;
		taskManagers = new ArrayList<ITask>();
		taskManagers.add(tasker);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
			
		//Check if Internet connection exist. 
	}
	
	@Override
	protected Boolean doInBackground(String... arg0) {

		try{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// Add your parameters
			nameValuePairs.add(new BasicNameValuePair(METHOD, req.getMethod()));
			nameValuePairs.add(new BasicNameValuePair(PARAMS, req.getParams()));
			
			//Add more parameters as necessary

			//Create the HTTP request
			HttpParams httpParameters = new BasicHttpParams();

			//Setup timeouts
			HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
			HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(req.getServerIP());
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			String result = EntityUtils.toString(entity);

			// Create a JSON object from the request response
			serverResponse = new JSONObject(result);

		}catch (Exception e){
			exception = e;
		}

		return true;
	}



	@Override
	protected void onPostExecute(Boolean valid){
		//On UI thread
		Utils.Log(TAG, "<onPostExecute>");
		if(taskManagers != null){
			for(ITask tasker : taskManagers){
				tasker.onTaskComplete(req , serverResponse.toString());
				Utils.Log(TAG, "Notify to :" + tasker.getClass().getName());
			}
		}
		if(exception != null)
			Utils.LogError(TAG, exception.getMessage());
		Utils.Log("DoPost", "</onPostExecute>");
	}

}