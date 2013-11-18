package syncme.app;

import syncme.app.ui.AppSectionsPagerAdapter;

import com.example.syncme.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class AuthenticationActivity extends Activity implements OnClickListener, OnEditorActionListener{
	
	Button authenticationButton;
	EditText authnticationInput;
	TextView explenationText;
	String authenticationCode = "", userName = "";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication_layout);
		
//		Intent intent = getIntent();
//		authenticationCode = intent.getStringExtra("AuthenticationCode");
//		userName = intent.getStringExtra("UserName");
		
		settings = getSharedPreferences("userDetails", 0);
		
		authenticationCode = settings.getString("AuthenticationCode", "");
		userName = settings.getString("UserName", "");
		
		authenticationButton = (Button) findViewById(R.id.authentication_button);
		authnticationInput = (EditText) findViewById(R.id.input_authentication);
		explenationText = (TextView) findViewById(R.id.authenticationText);
		
		userName =  userName.length() == 0?userName:userName.substring(0, 1).toUpperCase() + userName.substring(1);
		explenationText.setText(userName + ", \nA SMS has been send to you with an authentication number. \nPlease Enter the number here:");
		
		authenticationButton.setOnClickListener(this);
		authnticationInput.setOnEditorActionListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == authenticationButton.getId())
		{
			if (authnticationInput.getText().toString().compareTo(authenticationCode) == 0)
			{
				editor = settings.edit();
			    editor.putBoolean("firstTime", false);
			    editor.commit();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
//				Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Authentication Failed!  \nPlease try entering the authentication number again.", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE)
		{
			authenticationButton.performClick();
		}		
		
		return false;
	}

}
