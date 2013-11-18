package syncme.app;

import java.util.Random;

import syncme.app.logic.SMSReciver;

import com.example.syncme.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SignUpActivity extends Activity implements OnClickListener, OnEditorActionListener{
	
	EditText phoneumber, name;
	Button signUp;
	String AuthenticationCode = "", userName = "";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	boolean isFirst;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		settings = getSharedPreferences("userDetails", 0);
		isFirst = settings.getBoolean("firstTime", true);
		
		if (isFirst)
		{
			setContentView(R.layout.signup_layout);
			
			name = (EditText) findViewById(R.id.input_name);
			phoneumber = (EditText) findViewById(R.id.input_phone_number);
			
			signUp = (Button) findViewById(R.id.SignUp_button);
			
			signUp.setOnClickListener(this);
			phoneumber.setOnEditorActionListener(this);
			
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			
		}

	}


	@Override
	public void onClick(View v) {	
		if (v.getId() == signUp.getId())
		{
			userName = name.getText().toString();
			sendSMS(phoneumber.getText().toString());
			
			editor = settings.edit();
		    editor.putString("AuthenticationCode", AuthenticationCode);
		    editor.putString("UserName", userName);
		    editor.commit();
			Intent intent = new Intent(this, AuthenticationActivity.class);
//			intent.putExtra("AuthenticationCode", AuthenticationCode);
//			intent.putExtra("UserName", userName);
			startActivity(intent);
		}
	}


	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE)
		{
			signUp.performClick();
		}
		
		return false;
	}

	
	private void sendSMS(String phone) {

	    SmsManager smsManager = SmsManager.getDefault();
	    generateAuthenticationCode();
	    smsManager.sendTextMessage(phone, null, AuthenticationCode, null, null);
	}
	
	private void generateAuthenticationCode()
	{
		Random rand = new Random();
		AuthenticationCode =  rand.nextInt(Integer.MAX_VALUE) + "";
	}
}
