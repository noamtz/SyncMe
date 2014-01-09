package coupling.app;

import java.util.Date;

import com.nit.coupling.R;

import coupling.app.BL.BLShopListOverview;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AdapterShopListOverview extends CursorAdapter {

	 private LayoutInflater mLayoutInflater;
	 private Context mContext;
	
	 private BLShopListOverview blShopListOverview;
	 
	public AdapterShopListOverview(Context context, BLShopListOverview blShopListOverview) {
		super(context, blShopListOverview.getSource(), true);
		 mContext = context;
	     mLayoutInflater = LayoutInflater.from(context);
	     this.blShopListOverview = blShopListOverview;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		//Retrieve data from database
		long id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
		String title =  cursor.getString(cursor.getColumnIndexOrThrow("Title"));
		int totalItems = cursor.getInt(cursor.getColumnIndexOrThrow("TotalItems"));
		Date createdAt = Utils.toDate(cursor.getString(cursor.getColumnIndexOrThrow("CreatedAt")));
		
		TextView tvTitle = (TextView) row.findViewById(R.id.tvTitle);
		tvTitle.setText(title);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		 View v = mLayoutInflater.inflate(R.layout.shoplist_row, parent, false);
	     return v;
	}
	
	public void refreshList(){
		swapCursor(blShopListOverview.getSource());
		notifyDataSetChanged();
	}
}
