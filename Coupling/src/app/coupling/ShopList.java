package app.coupling;

import com.nit.coupling.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import app.coupling.data.DALShopList;

public class ShopList extends Activity implements OnClickListener, OnEditorActionListener{

	private TextView itemCountText;
	private EditText newItem;
	private Button plus, minus;
	private int itemQuantity;
	private ListView listItems;
	private AdapterShopList adapter;
	
	private long listId;
	
	private DALShopList dataSource = DALShopList.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_item_layout);
		
		listId = getIntent().getExtras().getLong("Id");
		String listTitle = getIntent().getExtras().getString("Title");
		
		itemCountText = (TextView) findViewById(R.id.count);
		newItem = (EditText) findViewById(R.id.input_item);
		plus = (Button) findViewById(R.id.plus_button);
		minus = (Button) findViewById(R.id.minus_button);
		
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		
		newItem.setOnEditorActionListener(this);
		
		listItems = (ListView)findViewById(R.id.shopping_list);
		Log.v("shoplist", "Total: " + dataSource.getSource(listId).getCount());
		adapter = new AdapterShopList(this, dataSource.getSource(listId), true);
		
		listItems.setAdapter(adapter);
		listItems.setSelection(listItems.getCount() - 1);
		
		itemQuantity = 1;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == plus.getId()) {
			itemQuantity ++;
			itemCountText.setText(Integer.toString(itemQuantity));
		} else if(v.getId() == minus.getId()) {
			if (itemQuantity > 1) {
				itemQuantity --;
				itemCountText.setText(Integer.toString(itemQuantity));
			}
		}
		
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_NEXT) {
			if (newItem.getText().length() > 0){
				boolean isSucceed = dataSource.addItem(listId ,newItem.getText().toString() , itemQuantity); 
				if(!isSucceed)
					Log.e("shoplist", "failed to add an item");
				refreshList();
			}
				
			listItems.setSelection(listItems.getCount() - 1);
			
			newItem.setText("");

			itemQuantity = 1;
			itemCountText.setText(Integer.toString(itemQuantity));
			
			return true;
		}
		return false;
	}
	
	private void refreshList(){
		adapter.swapCursor(dataSource.getSource(listId));
		adapter.notifyDataSetChanged();
	}
	
}
