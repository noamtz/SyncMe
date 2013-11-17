package syncme.app.ui;

import java.util.List;

import syncme.app.model.shoplist.Item;
import syncme.app.model.shoplist.ShopList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ItemListAddaptor extends ArrayAdapter<Item>{

	private List<Item> items;
	private int layoutResourceId;
	private Context context;
	private ShopList sl;

	
	public ItemListAddaptor(Context context, int resource, ShopList sl) {
		super(context, resource , sl.getItems());
		this.sl = sl;
		this.layoutResourceId = resource;
		this.context = context;
		this.items = sl.getItems();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		boolean isChecked;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		
        if (row == null) {
        	row = inflater.inflate(layoutResourceId, null);

            // Set the click listener for the checkbox
            
        }
        
        
        Item item = (Item)items.get(position);
        
        Button x_button = (Button) row.findViewById(com.example.syncme.R.id.x_button);
        x_button.setTag(item);
        
        final TextView itemName = (TextView) row.findViewById(com.example.syncme.R.id.item_name);
        itemName.setText(item.getName());
        
        
        TextView count = (TextView) row.findViewById(com.example.syncme.R.id.item_count);
        count.setText(item.getQuantity()+ "");
        
        CheckBox check = (CheckBox) row.findViewById(com.example.syncme.R.id.item_check);
        isChecked = item.isDone();
        check.setChecked(isChecked);
        check.setTag(item);
        
        row.findViewById(com.example.syncme.R.id.item_check).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Item data = (Item) v.getTag();
		        data.setDone(((CheckBox) v).isChecked());
		        sl.updateItem(data);
		        if (((CheckBox) v).isChecked())
		        {
					itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					itemName.setTextColor(Color.GRAY);
		        } else {
					itemName.setPaintFlags(itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
					itemName.setTextColor(Color.WHITE);
		        }
			}
		});
        

        
        if (isChecked)
        {
			itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			itemName.setTextColor(Color.GRAY);
        } else {
			itemName.setPaintFlags(itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
			itemName.setTextColor(Color.WHITE);
        }
        
		return row;
	}


}
