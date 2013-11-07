package syncme.app.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ItemListAddaptor extends ArrayAdapter<Item>{

	private List<Item> items;
	private int layoutResourceId;
	private Context context;
	

	
	public ItemListAddaptor(Context context, int resource, List<Item> objects) {
		super(context, resource, objects);
		
		this.layoutResourceId = resource;
		this.context = context;
		this.items = objects;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		final ItemtHolder holder = new ItemtHolder();

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		
		if (row == null)
			row = inflater.inflate(layoutResourceId, parent, false);
		
		holder.item = items.get(position);
		
		holder.itemName = (TextView) row.findViewById(com.example.syncme.R.id.item_name);
		holder.count = (TextView) row.findViewById(com.example.syncme.R.id.item_count);
		holder.check = (CheckBox) row.findViewById(com.example.syncme.R.id.item_check);
		
		holder.itemName.setText(holder.item.getItem());
		holder.count.setText(holder.item.getCount() + "");
		
		holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
				{
					holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					holder.itemName.setTextColor(Color.GRAY);
				} else {
					holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
					holder.itemName.setTextColor(Color.WHITE);
				}
			}
		});
		
		row.setTag(holder);
		
		return row;
	}
	
	public static class ItemtHolder {
		Item item;
		TextView itemName;
		TextView count;
		CheckBox check;	
	}




}
