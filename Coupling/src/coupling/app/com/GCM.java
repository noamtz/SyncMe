package coupling.app.com;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import coupling.app.App;
import coupling.app.Utils;

public class GCM {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "69286130837";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;
    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(App.getAppCtx());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, App.getAppCtx(),
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.i(TAG, "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
    
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(App.getAppCtx());
					}
					regid = gcm.register(SENDER_ID);

					storeRegistrationId(regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				SharedPreferences prefs = Utils.getSPF();
				prefs.edit().putBoolean(Constants.IS_REGISTERED, true);
				prefs.edit().putBoolean(Constants.IS_REGISTERED, true);
				App.loadOwner();
			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(String regId) {
		final SharedPreferences prefs = Utils.getSPF();
		int appVersion = getAppVersion();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.REG_ID, regId);
		editor.putInt(Constants.APP_VERSION, appVersion);
		editor.commit();
	}

	private static int getAppVersion() {
		try {
			PackageInfo packageInfo = App.getAppCtx().getPackageManager()
					.getPackageInfo(App.getAppCtx().getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

}
