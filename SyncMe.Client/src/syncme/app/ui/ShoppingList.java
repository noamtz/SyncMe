package syncme.app.ui;

import java.util.ArrayList;

import com.example.syncme.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ShoppingList extends Activity implements OnClickListener, OnEditorActionListener{
	
	private ItemListAddaptor adapter;
	TextView itemCountText;
	EditText newItem;
	Button plus, minus;
	int itemCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_list_layout);
		
		itemCountText = (TextView) findViewById(R.id.count);
		newItem = (EditText) findViewById(R.id.input_item);
		plus = (Button) findViewById(R.id.plus_button);
		minus = (Button) findViewById(R.id.minus_button);
		
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		
		newItem.setOnEditorActionListener(this);
		
		itemCount = 1;
		
		adapter = new ItemListAddaptor(this, R.layout.list_item_layout, new ArrayList<Item>());
		ListView ItemListView = (ListView)findViewById(R.id.shopping_list);
		ItemListView.setAdapter(adapter);	
		
		
		
		adapter.insert(new Item("Eggs", 1), 0);
		adapter.insert(new Item("Bread", 1), 0);
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
				adapter.add(new Item(newItem.getText() + "", itemCount));
				
			
			newItem.setText("");

			itemCount = 1;
			itemCountText.setText(itemCount + "");
			
			return true;
		}
		return false;
	}



}
