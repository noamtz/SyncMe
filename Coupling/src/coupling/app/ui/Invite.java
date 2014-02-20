package coupling.app.ui;

import com.nit.coupling.R;

import coupling.app.Utils;
import coupling.app.com.API;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * This class take care of the invite friend UI
 * it also have a rich auto selected friend from the contacts
 * of the phone
 * @author Noam Tzumie
 *
 */
public class Invite extends Activity implements OnClickListener{

	AutoCompleteTextView frientAC;
	Button inviteBT, cancel;
	API api	= API.getInstance();;
	ContentResolver content;
	boolean flag = true;
	String phoneNumber = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_layout);

		content = getContentResolver();
		Cursor cursor = content.query(Contacts.CONTENT_URI,
				CONTACT_PROJECTION, null, null, null);

		ContactListAdapter adapter = new ContactListAdapter(this, cursor);

		frientAC = (AutoCompleteTextView) findViewById(R.id.friend_ac);
		frientAC.setAdapter(adapter);


		inviteBT = (Button) findViewById(R.id.invite_bt);
		inviteBT.setOnClickListener(this);

		cancel = (Button) findViewById(R.id.invite_cancel_bt);
		cancel.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {

		if (v.getId() == inviteBT.getId()){
			String friendEmail = frientAC.getText().toString();
			if (friendEmail.length() > 0){

				ContentResolver cr = getContentResolver();
				//get Contact List
				Cursor c2 = cr.query(ContactsContract.Contacts.CONTENT_URI, null, 
						"DISPLAY_NAME = '" + frientAC.getText().toString() + "'" ,
						null, null);
				if (c2.moveToFirst()) {
					String Contactid = c2.getString(c2.getColumnIndex(ContactsContract.Contacts._ID));
					
					//get contacts number by name
					Cursor c3 = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + Contactid, null, null);
					while (c3.moveToNext() && flag) {
						String pNumber = c3.getString(c3.getColumnIndex(Phone.NUMBER));
						int type = c3.getInt(c3.getColumnIndex(Phone.TYPE));
						switch(type) {
						case Phone.TYPE_HOME:
							phoneNumber = pNumber;
							break;
						case Phone.TYPE_MOBILE:
							flag = false;
							phoneNumber = pNumber;
							break;
						case Phone.TYPE_WORK:
							phoneNumber = pNumber;
							break;
						default:
							phoneNumber = pNumber;

							break;
						}
					}
				}

				//If not from auto complete
				if(phoneNumber.isEmpty()){
					phoneNumber = friendEmail;
				}

				//Reformat text to contain just numbers
				phoneNumber = phoneNumber.replaceAll("[^0-9]+", "");

				//if 972 if start of number
				if (phoneNumber.indexOf("972") == 0){
					phoneNumber = 0 + phoneNumber.substring(3);
				} 	
				if (Utils.isStringANumber(phoneNumber)){
					Utils.Log("DEBUG", "true");

					//Send api request
					api.invite(phoneNumber);

					finish();
				} else {
					Utils.showToast("Contact with no phone number");
				}

			} else {
				Utils.showToast("Please Enter Friends Number");
			}
		} else if (v.getId() == cancel.getId()){
			finish();
		}
	}

	// XXX compiler bug in javac 1.5.0_07-164, we need to implement Filterable
	// to make compilation work
	public static class ContactListAdapter extends CursorAdapter implements Filterable {
		public ContactListAdapter(Context context, Cursor c) {
			super(context, c);
			mContent = context.getContentResolver();
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final TextView view = (TextView) inflater.inflate(
					android.R.layout.simple_dropdown_item_1line, parent, false);
			view.setText(cursor.getString(COLUMN_DISPLAY_NAME));

			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((TextView) view).setText(cursor.getString(COLUMN_DISPLAY_NAME));
		}

		@Override
		public String convertToString(Cursor cursor) {
			return cursor.getString(COLUMN_DISPLAY_NAME);
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			FilterQueryProvider filter = getFilterQueryProvider();
			if (filter != null) {
				return filter.runQuery(constraint);
			}

			Uri uri = Uri.withAppendedPath(
					Contacts.CONTENT_FILTER_URI,
					Uri.encode(constraint.toString()));
			return mContent.query(uri, CONTACT_PROJECTION, null, null, null);
		}

		private ContentResolver mContent;
	}

	public static final String[] CONTACT_PROJECTION = new String[] {
		Contacts._ID,
		Contacts.DISPLAY_NAME
	};

	private static final int COLUMN_DISPLAY_NAME = 1;

}
