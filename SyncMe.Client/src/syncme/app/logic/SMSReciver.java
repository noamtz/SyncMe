package syncme.app.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
//			Toast.makeText(context, "SMS Recived", Toast.LENGTH_SHORT).show();
			// Retrieves a map of extended data from the intent.
			final Bundle bundle = intent.getExtras();
			 
			try {
			     
			    if (bundle != null) {
			         
			        final Object[] pdusObj = (Object[]) bundle.get("pdus");
			         
			        for (int i = 0; i < pdusObj.length; i++) {
			             
			            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
			            String phoneNumber = currentMessage.getDisplayOriginatingAddress();
			             
			            String senderNum = phoneNumber;
			            String message = currentMessage.getDisplayMessageBody().trim();
			 
			            Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
			             
			            Toast.makeText(context, "senderNum: " + message + "  message" , Toast.LENGTH_LONG).show();
			            if (message.equals("Hello"))
			            {
			            	Toast.makeText(context, "senderNum: " + message , Toast.LENGTH_LONG).show();
			            }
			            //Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, Toast.LENGTH_LONG).show();
			            
			             
			        } // end for loop
			      } // bundle is null
			 
			} catch (Exception e) {
			    Log.e("SmsReceiver", "Exception smsReceiver" +e);
			     
			}
		}

	}

}
