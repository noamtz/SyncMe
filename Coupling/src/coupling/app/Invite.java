package coupling.app;

import com.nit.coupling.R;

import coupling.app.com.API;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Invite extends Activity implements OnClickListener{

	EditText friendET;
	Button inviteBT;
	API api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_layout);
		
		friendET = (EditText) findViewById(R.id.friend_et);
		
		inviteBT = (Button) findViewById(R.id.invite_bt);
		inviteBT.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {

		if (v.getId() == inviteBT.getId()){
			String friendEmail = friendET.getText().toString();
			if (friendEmail.length() > 0){
				//TODO:API.invite();
				
				api = API.getInstance();
				api.invite(friendEmail);
				
			} else {
				Utils.shopToast("Please Enter Friends Email");
			}
		}
	}

}
