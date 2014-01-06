package coupling.app;

import com.nit.coupling.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ShopList extends Activity{

	private TextView tvItemQuantity;
	private EditText etItemName;
	private Button plus, minus, add;

	private ListView listItems;
	private AdapterShopList adapter;	

	private Ids selectedItemIds;
	private int itemQuantity;

	private BLShopList blShopList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_item_layout);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		long listId = getIntent().getExtras().getLong("Id");
		String listTitle = getIntent().getExtras().getString("Title");

		initGui();

		blShopList = new BLShopList(listId);

		adapter = new AdapterShopList(this, blShopList);

		listItems.setAdapter(adapter);
		listItems.setSelection(listItems.getCount() - 1);

		itemQuantity = 1;
		selectedItemIds = null;


	}

	private void initGui(){ 
		tvItemQuantity = (TextView) findViewById(R.id.count);
		etItemName = (EditText) findViewById(R.id.input_item);
		plus = (Button) findViewById(R.id.plus_button);
		minus = (Button) findViewById(R.id.minus_button);
		add = (Button) findViewById(R.id.add_button);
		listItems = (ListView)findViewById(R.id.shopping_list);

		plus.setOnClickListener(increaseQuantity());
		minus.setOnClickListener(decreaseQuantity());
		add.setOnClickListener(addItem());
		etItemName.setOnEditorActionListener(itemHandler());
		listItems.setOnItemClickListener(editItem());
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
				String globalId = cursor.getString(cursor.getColumnIndex("UId"));
				selectedItemIds = new Ids(dbId, globalId);

				String itemName = cursor.getString(cursor.getColumnIndex("ItemName"));
				Integer itemQuantity = cursor.getInt(cursor.getColumnIndex("ItemQuantity"));

				etItemName.setText(itemName);
				tvItemQuantity.setText(itemQuantity.toString());

				Log.v("", "selected: " + itemName);
			}
		};
	}


	public void addItemToList()
	{
		if (etItemName.getText().length() > 0){
			if(selectedItemIds == null){
				boolean isSucceed = blShopList.createItem(etItemName.getText().toString() , itemQuantity); 
				if(!isSucceed)
					Log.e("shoplist", "failed to add an item");
			}
			else{
				boolean isSucceed = blShopList.updateItem(selectedItemIds ,etItemName.getText().toString() , itemQuantity, null);
				if(!isSucceed)
					Log.e("shoplist", "failed to update an item with id: " + selectedItemIds);
			}
			adapter.refresh();
		}

		listItems.setSelection(listItems.getCount() - 1);

		etItemName.setText("");
		selectedItemIds = null;
		itemQuantity = 1;
		tvItemQuantity.setText(Integer.toString(itemQuantity));
	}
}
