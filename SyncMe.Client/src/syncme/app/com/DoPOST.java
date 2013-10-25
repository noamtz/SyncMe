package syncme.app.com;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import syncme.app.data.Request;
import android.os.AsyncTask;
import static syncme.app.logic.Constants.*;

public class DoPOST extends AsyncTask<String, Void, Boolean>{

	//Check if an exception has thrown
	Exception exception = null;
	
	JSONObject serverResponse;
	
	ITask taskManager;
	Request req;
	
	public DoPOST(Request req , ITask taskManager){
		this.req = req;
		this.taskManager = taskManager;
		
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
		if(taskManager != null){
			taskManager.onTaskComplete(req.getId() , serverResponse.toString());
		}
	}

}