package coupling.app;

import java.util.LinkedList;

import com.nit.coupling.R;

import coupling.app.BL.BLFactory;
import coupling.app.BL.BLGroceryList;
import coupling.app.BL.BLShopList;
import coupling.app.com.IBLConnector;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ShopList extends Activity implements IBLConnector{

	private TextView tvItemQuantity;
	private AutoCompleteTextView acItemName;
	private Button plus, minus, add;

	private ListView listItems;
	private AdapterShopList adapter;	

	private Ids selectedItemIds;
	private int itemQuantity;

	private BLShopList blShopList;
	private GroceryList groceryList;
	
	private LinkedList<String> grocerys;
	
	private ArrayAdapter<String> ACadapter ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_item_layout);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		long listId = getIntent().getExtras().getLong("Id");
		String listTitle = getIntent().getExtras().getString("Title");
		setTitle(listTitle);
		
		groceryList = new GroceryList();
		groceryList.loadItems();
	
		initGui();

		blShopList = BLFactory.getInstance().getShopList(listId);
		blShopList.setBLConnector(this);
		
		adapter = new AdapterShopList(this, blShopList);

		listItems.setAdapter(adapter);
		listItems.setSelection(listItems.getCount() - 1);

		itemQuantity = 1;
		selectedItemIds = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		blShopList.unsetBLConnector();
	}

	@Override
	protected void onResume() {
		super.onResume();
		blShopList.setBLConnector(this);
	}
	
	private void initGui(){ 
		
		grocerys = groceryList.getGroceryList();
		ACadapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, grocerys);
		
		tvItemQuantity = (TextView) findViewById(R.id.count);
		acItemName = (AutoCompleteTextView) findViewById(R.id.input_item);
		plus = (Button) findViewById(R.id.plus_button);
		minus = (Button) findViewById(R.id.minus_button);
		add = (Button) findViewById(R.id.add_button);
		listItems = (ListView)findViewById(R.id.shopping_list);

		plus.setOnClickListener(increaseQuantity());
		minus.setOnClickListener(decreaseQuantity());
		add.setOnClickListener(addItem());
		acItemName.setOnEditorActionListener(itemHandler());
		listItems.setOnItemClickListener(editItem());
		
		acItemName.setAdapter(ACadapter);
		acItemName.setThreshold(1);
	}

	private OnClickListener increaseQuantity(){
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				itemQuantity ++;
				tvItemQuantity.setText(Integer.toString(itemQuantity));
			}
		};
	}

	private OnClickListener decreaseQuantity(){
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (itemQuantity > 1) {
					itemQuantity --;
					tvItemQuantity.setText(Integer.toString(itemQuantity));
				}
			}
		};
	}

	private OnClickListener addItem(){
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				addItemToList();
			}
		};
	}

	private OnEditorActionListener itemHandler(){
		return new OnEditorActionListener() {


			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					addItemToList();
					return true;
				}
				return false;
			}
		};
	}

	private OnItemClickListener editItem(){
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				Cursor cursor = (Cursor) adapter.getItem(position);

				long dbId = cursor.getLong(cursor.getColumnIndex("_id"));
				Long globalId = cursor.getLong(cursor.getColumnIndex("UId"));
				selectedItemIds = new Ids(dbId, globalId);

				String itemName = cursor.getString(cursor.getColumnIndex("ItemName"));
				itemQuantity = cursor.getInt(cursor.getColumnIndex("ItemQuantity"));

				acItemName.setText(itemName);
				tvItemQuantity.setText(itemQuantity + "");

				Log.v("", "selected: " + itemName);
			}
		};
	}


	public void addItemToList()
	{
		if (acItemName.getText().length() > 0){
			if(selectedItemIds == null){
				groceryList.addItem(acItemName.getText().toString() ,ACadapter);
				boolean isSucceed = blShopList.createItem(acItemName.getText().toString() , itemQuantity); 
				if(!isSucceed)
					Log.e("shoplist", "failed to add an item");
			}
			else{
				boolean isSucceed = blShopList.updateItem(selectedItemIds ,acItemName.getText().toString() , itemQuantity, null);
				if(!isSucceed)
					Log.e("shoplist", "failed to update an item with id: " + selectedItemIds);
			}
			adapter.refresh();
		}

		listItems.setSelection(listItems.getCount() - 1);

		acItemName.setText("");
		selectedItemIds = null;
		itemQuantity = 1;
		tvItemQuantity.setText(Integer.toString(itemQuantity));
	}

	@Override
	public void Refresh() {	
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.refresh();
				Utils.Log("ShopList", "*** ON REFRESH **");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return false;	
	}
}