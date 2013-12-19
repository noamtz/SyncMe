package coupling.app;

import com.nit.coupling.R;

import coupling.app.data.DALShopList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AdapterShopList extends CursorAdapter {

	private LayoutInflater mLayoutInflater;
	
	private DALShopList dataSource;
	
	
	public AdapterShopList(Context context, DALShopList dataSource) {
		super(context, dataSource.getSource(), true);
		mLayoutInflater = LayoutInflater.from(context); 
		
		this.dataSource = dataSource;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		//Retrieve data from database
		long id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
		String name =  cursor.getString(cursor.getColumnIndexOrThrow("ItemName"));
		int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("ItemQuantity"));
		boolean isDone = cursor.getInt(cursor.getColumnIndexOrThrow("ItemStatus")) > 0;

		//Construct views
		Button btnRemoveItem = (Button)row.findViewById(R.id.btnRemoveItem);
		btnRemoveItem.setTag(id);

		final TextView itemName = (TextView) row.findViewById(R.id.item_name);
		itemName.setText(name);

		TextView itemQuantity = (TextView) row.findViewById(R.id.item_count);
		itemQuantity.setText(Integer.toString(quantity));

		CheckBox check = (CheckBox) row.findViewById(R.id.item_check);
		check.setChecked(isDone);
		check.setTag(id);

		btnRemoveItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isSucceed = dataSource.deleteItem((Long)v.getTag());
				if(!isSucceed)
					Log.e("adaptor_shoplist", "failed to delete item: " + (Long)v.getTag());
				refresh();
			}
		});

		//Set checkbox action
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean isSucceed = dataSource.updateItem((Long)buttonView.getTag(), null, null, isChecked);
				if(!isSucceed)
					Log.e("adaptor_shoplist", "failed to update item");
				if(isChecked){
					itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					itemName.setTextColor(Color.GRAY);
				} else{
					itemName.setPaintFlags(itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
					itemName.setTextColor(Color.WHITE);
				}
				refresh();
			}
		});
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.shoplist_item, parent, false);
	}

	public void refresh(){
		swapCursor(dataSource.getSource());
		notifyDataSetChanged();
	}

}