package coupling.app.ui;



import com.nit.coupling.R;

import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.BL.BLShopList;
import coupling.app.models.ShopListItem;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterShopList extends CursorAdapter {

	private LayoutInflater mLayoutInflater;

	private BLShopList blShoplist;


	public AdapterShopList(Context context, BLShopList blShoplist) {
		super(context, blShoplist.getSource(), true);
		mLayoutInflater = LayoutInflater.from(context); 

		this.blShoplist = blShoplist;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		//Retrieve data from database
		ShopListItem item = new ShopListItem(cursor);
		//Construct views
		Button btnRemoveItem = (Button)row.findViewById(R.id.btnRemoveItem);
		btnRemoveItem.setTag(item.getIds());

		final TextView itemName = (TextView) row.findViewById(R.id.item_name);
		itemName.setText(item.getName());

		TextView itemQuantity = (TextView) row.findViewById(R.id.item_count);
		itemQuantity.setText(Integer.toString(item.getQuantity()));

		CheckBox check = (CheckBox) row.findViewById(R.id.item_check);
		check.setChecked(item.isDone());
		check.setTag(item.getIds());

		final ImageView redLine = (ImageView) row.findViewById(R.id.red_line_view);
		TextView tvStatue = (TextView) row.findViewById(R.id.tvItemStatus);

		//Lock UI componenet and paint in red until global Id is updated
		if(item.isLocked()) {
			tvStatue.setVisibility(View.VISIBLE);
			tvStatue.setBackgroundColor(Color.RED);

			btnRemoveItem.setEnabled(false);
		} 
		else {
			//Paint in yellow if the item is not mine
			if(!item.isMine()){
				tvStatue.setVisibility(View.VISIBLE);
				tvStatue.setBackgroundColor(Color.YELLOW);
			}
			else {
				tvStatue.setVisibility(View.GONE);
				tvStatue.setBackgroundColor(Color.WHITE);
			}
			btnRemoveItem.setEnabled(true);
		}
		//Mark with red line to notify that the item is checked 
		if (item.isDone())
		{
			redLine.setVisibility(View.VISIBLE);
		} else {
			redLine.setVisibility(View.INVISIBLE);
		}
		//Remove item from list
		btnRemoveItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				blShoplist.deleteItem((Ids)v.getTag());
				refresh();
			}
		});

		//Set checkbox action
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ShopListItem item = new ShopListItem(blShoplist.getShopListId() , (Ids)buttonView.getTag());
				item.setIsDone(isChecked);
				boolean isUpdated = blShoplist.updateItem(item);

				if(isUpdated){
					Utils.Log("AdapterShopList", "Update isDone: " + isChecked);
					if(isChecked){
						redLine.setVisibility(View.VISIBLE);
					} else{
						redLine.setVisibility(View.INVISIBLE);
					}
				}
				refresh();
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.shoplist_item, parent, false);
	}
	
	/**
	 * Refresh list data & UI
	 */
	public void refresh(){
		swapCursor(blShoplist.getSource());
		notifyDataSetChanged();
	}

}
