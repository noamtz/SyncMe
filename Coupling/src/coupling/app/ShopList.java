package coupling.app;

import com.nit.coupling.R;

import coupling.app.data.DALShopList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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
	private Button plus, minus;

	private ListView listItems;
	private AdapterShopList adapter;	

	private long selectedItemId;
	private int itemQuantity;

	private DALShopList dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_item_layout);

		long listId = getIntent().getExtras().getLong("Id");
		String listTitle = getIntent().getExtras().getString("Title");

		initGui();
		
		dataSource = new DALShopList(listId);
		
		adapter = new AdapterShopList(this, dataSource);
		
		listItems.setAdapter(adapter);
		listItems.setSelection(listItems.getCount() - 1);

		itemQuantity = 1;
		selectedItemId = -1;


	}
	
	private void initGui(){ 
		tvItemQuantity = (TextView) findViewById(R.id.count);
		etItemName = (EditText) findViewById(R.id.input_item);
		plus = (Button) findViewById(R.id.plus_button);
		minus = (Button) findViewById(R.id.minus_button);
		listItems = (ListView)findViewById(R.id.shopping_list);
		
		plus.setOnClickListener(increaseQuantity());
		minus.setOnClickListener(decreaseQuantity());
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

	private OnEditorActionListener itemHandler(){
		return new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					if (etItemName.getText().length() > 0){
						if(selectedItemId == -1){
							boolean isSucceed = dataSource.addItem(etItemName.getText().toString() , itemQuantity); 
							if(!isSucceed)
								Log.e("shoplist", "failed to add an item");
						}
						else{
							boolean isSucceed = dataSource.updateItem(selectedItemId ,etItemName.getText().toString() , itemQuantity, null);
							if(!isSucceed)
								Log.e("shoplist", "failed to update an item with id: " + selectedItemId);
						}
						adapter.refresh();
					}

					listItems.setSelection(listItems.getCount() - 1);

					etItemName.setText("");
					selectedItemId = -1;
					itemQuantity = 1;
					tvItemQuantity.setText(Integer.toString(itemQuantity));

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

				selectedItemId = cursor.getLong(cursor.getColumnIndex("_id"));
				String itemName = cursor.getString(cursor.getColumnIndex("ItemName"));
				Integer itemQuantity = cursor.getInt(cursor.getColumnIndex("ItemQuantity"));

				etItemName.setText(itemName);
				tvItemQuantity.setText(itemQuantity.toString());
				
				Log.v("", "selected: " + itemName);
			}
		};
	}
}
