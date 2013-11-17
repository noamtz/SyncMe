package syncme.app.ui;

import java.util.ArrayList;

import syncme.app.data.DAL;
import syncme.app.logic.SMSReciver;
import syncme.app.model.shoplist.Item;
import syncme.app.model.shoplist.ShopList;
import syncme.app.utils.CommonUtils;

import com.example.syncme.R;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class ShoppingList extends Activity implements OnClickListener, OnEditorActionListener{
	
	private ItemListAddaptor adapter;
	TextView itemCountText;
	EditText newItem;
	Button plus, minus;
	int itemCount;
	ListView ItemListView;
	SMSReciver sms = new SMSReciver();
	Long id;
	ShopList sl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_list_layout);
		CommonUtils.Log("ShoppingList",""+ getIntent().getExtras().getLong("Id"));
		
		id = getIntent().getExtras().getLong("Id");
		sl = DAL.getInstance().getShopList().getShopList(id);
		
		itemCountText = (TextView) findViewById(R.id.count);
		newItem = (EditText) findViewById(R.id.input_item);
		plus = (Button) findViewById(R.id.plus_button);
		minus = (Button) findViewById(R.id.minus_button);
		
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		
		newItem.setOnEditorActionListener(this);
		
		itemCount = 1;
		
		adapter = new ItemListAddaptor(this, R.layout.list_item_layout, sl.getItems());
		ItemListView = (ListView)findViewById(R.id.shopping_list);
		ItemListView.setAdapter(adapter);	
		ItemListView.setSelection(ItemListView.getCount() - 1);
		
	
		
		IntentFilter i = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(sms, i);
		
		sl = DAL.getInstance().getShopList().getShopList(id);
		
		//sendSMS();
		
	
		

	}

	public void sendSMS() {

	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage("+972544813486", null, " Hello ", null, null);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == plus.getId())
		{
			itemCount ++;
			itemCountText.setText(itemCount + "");
		} else if (v.getId() == minus.getId())
		{
			if (itemCount > 1)
			{
				itemCount --;
				itemCountText.setText(itemCount + "");
			}
		} 
		
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_NEXT)
		{
			if (newItem.getText().length() > 0)
			{
				Item item = new Item(newItem.getText() + "", itemCount);
				sl.addItem(item);
				adapter.add(item);
			}
				
			ItemListView.setSelection(ItemListView.getCount() - 1);
			
			newItem.setText("");

			itemCount = 1;
			itemCountText.setText(itemCount + "");
			
			return true;
		}
		return false;
	}

	public void xClick(View v)
	{
		Item item = (Item)v.getTag();
		sl.deleteItem(item);
		adapter.remove(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(sms);
	}
	
	
}
