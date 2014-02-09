package coupling.app.ui;

import java.util.LinkedList;

import com.nit.coupling.R;

import coupling.app.GroceryList;
import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.BL.BLFactory;
import coupling.app.BL.BLGroceryList;
import coupling.app.BL.BLShopList;
import coupling.app.com.IBLConnector;
import coupling.app.models.ShopListItem;

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

import static coupling.app.data.Constants.*;

public class ShopList extends Activity implements IBLConnector{

	private static final String TAG = "ShopList";
	
	private static final int DEFUALT_ITEM_QUANTITY = 1;
	private TextView tvItemQuantity;
	private AutoCompleteTextView acItemName;
	private Button plus, minus, add;

	private ListView listItems;
	private AdapterShopList adapter;	

	private int itemQuantity;
	private ShopListItem selectedItem;

	private BLShopList blShopList;
	private GroceryList groceryList;
	
	private long listId;
	
	private LinkedList<String> grocerys;
	
	private ArrayAdapter<String> ACadapter ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_item_layout);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//Get list information from shopListOverview section
		listId = getIntent().getExtras().getLong(SHOPLIST_ID);
		String listTitle = getIntent().getExtras().getString("Title");
		setTitle(listTitle);
		
		groceryList = GroceryList.getInstance();
		groceryList.loadItems();
		
		initGui();
		
		blShopList = BLFactory.getInstance().getShopList(listId);
		blShopList.setBLConnector(this);
		
		adapter = new AdapterShopList(this, blShopList);

		listItems.setAdapter(adapter);
		//Focus on the last item on list for UX
		listItems.setSelection(listItems.getCount() - 1);

		itemQuantity = 1;
		selectedItem = null;
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
		adapter.refresh();
	}
	
	/**
	 * Initialize all GUI components
	 */
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
				tvItemQuantity.setText(Integer.toString(++itemQuantity));
			}
		};
	}

	private OnClickListener decreaseQuantity(){
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (itemQuantity > 1) {
					tvItemQuantity.setText(Integer.toString(--itemQuantity));
				}
			}
		};
	}

	private OnClickListener addItem(){
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemAction();
			}
		};
	}

	private OnEditorActionListener itemHandler(){
		return new OnEditorActionListener() {


			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					itemAction();
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

				long dbId = cursor.getLong(cursor.getColumnIndex(ID));
				Long globalId = cursor.getLong(cursor.getColumnIndex(UID));
				selectedItem = new ShopListItem(listId, new Ids(dbId, globalId));
				
				selectedItem.setName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
				selectedItem.setQuantity(cursor.getInt(cursor.getColumnIndex(ITEM_QUANTITY)));

				acItemName.setText(selectedItem.getName());
				tvItemQuantity.setText(Integer.toString(selectedItem.getQuantity()));
				Utils.Log(TAG, "editItem", "Selected: " + selectedItem);
			}
		};
	}

	/**
	 * If no item is choosen create new item otherwise update
	 * the selected item
	 */
	public void itemAction()
	{
		if (acItemName.getText().length() > 0){
			if(selectedItem == null){
				boolean isItemAdded =blShopList.createItem(acItemName.getText().toString() , itemQuantity);
				if(isItemAdded)
					groceryList.addItem(acItemName.getText().toString() ,ACadapter);
				
			}
			else{
				selectedItem.setName(acItemName.getText().toString());
				selectedItem.setQuantity(Integer.parseInt(tvItemQuantity.getText().toString()));
				
				blShopList.updateItem(selectedItem);
			}
			adapter.refresh();
		}

		listItems.setSelection(listItems.getCount() - 1);

		//Reset Session
		acItemName.setText("");
		selectedItem = null;
		
		itemQuantity = DEFUALT_ITEM_QUANTITY;
		tvItemQuantity.setText(Integer.toString(itemQuantity));
	}

	/**
	 * Refresh list (activated also from BL)
	 */
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