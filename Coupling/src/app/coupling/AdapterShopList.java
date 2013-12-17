package app.coupling;

import com.nit.coupling.R;

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
import android.widget.CursorAdapter;
import android.widget.TextView;
import app.coupling.data.DALShopList;

public class AdapterShopList extends CursorAdapter {

	 private LayoutInflater mLayoutInflater;
	 private Context mContext;
	
	public AdapterShopList(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		 mContext = context;
	     mLayoutInflater = LayoutInflater.from(context); 
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		//Retrieve data from database
		long id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
		String name =  cursor.getString(cursor.getColumnIndexOrThrow("ItemName"));
		int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("ItemQuantity"));
		boolean isDone = cursor.getInt(cursor.getColumnIndexOrThrow("ItemStatus")) > 0;
		
		//Construct views
		Button removeItemBtn = (Button)row.findViewById(R.id.x_button);
		removeItemBtn.setTag(id);
		
		final TextView itemName = (TextView) row.findViewById(R.id.item_name);
        itemName.setText(name);
        
        TextView itemQuantity = (TextView) row.findViewById(R.id.item_count);
        itemQuantity.setText(Integer.toString(quantity));
        
        CheckBox check = (CheckBox) row.findViewById(R.id.item_check);
        check.setChecked(isDone);
        check.setTag(id);
        
        //Set checkbox action
        check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox activeCheck = (CheckBox) v;
				boolean isSucceed = DALShopList.getInstance().updateItem((Long)activeCheck.getTag(), null, null, activeCheck.isChecked());
				if(!isSucceed)
					Log.e("adaptor_shoplist", "failed to update item");
				
				if(activeCheck.isChecked()){
					itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					itemName.setTextColor(Color.GRAY);
				} else {
					itemName.setPaintFlags(itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
					itemName.setTextColor(Color.WHITE);
				}
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		 View v = mLayoutInflater.inflate(R.layout.shoplist_item, parent, false);
	     return v;
	}

}
