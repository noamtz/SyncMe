package coupling.app.ui;

import com.nit.coupling.R;

import coupling.app.Ids;
import coupling.app.Utils;
import coupling.app.BL.BLShopListOverview;
import coupling.app.models.ShopListOverView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AdapterShopListOverview extends CursorAdapter {

	private static final String TAG = "AdapterShopListOverview";

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
		ShopListOverView listov = new ShopListOverView(cursor);

		TextView tvNotMine = (TextView) row.findViewById(R.id.tvNotMine);
		TextView tvIsLocked = (TextView) row.findViewById(R.id.tvIsLock);
		TextView tvTitle = (TextView) row.findViewById(R.id.tvTitle);
		Button removeList = (Button) row.findViewById(R.id.btnRemoveList);


		if(!listov.isMine()){
			Utils.Log(TAG, "List: " + listov.getTitle() + ", is mine?=" + listov.isMine());
			tvNotMine.setVisibility(View.VISIBLE);
			tvNotMine.setBackgroundColor(Color.YELLOW);
		} else {
			Utils.Log(TAG, "List: " + listov.getTitle() + ", is mine?=" + listov.isMine());
			tvNotMine.setVisibility(View.GONE);
			tvNotMine.setBackgroundColor(Color.WHITE);
		}


		//Check if item is locked
		if(listov.isLocked()){
			tvIsLocked.setVisibility(View.VISIBLE);
			tvIsLocked.setBackgroundColor(Color.RED);

			removeList.setEnabled(false);
		} else {
			tvIsLocked.setVisibility(View.GONE);
			tvIsLocked.setBackgroundColor(Color.WHITE);

			removeList.setEnabled(true);
		}


		tvTitle.setText(listov.getTitle());

		removeList.setTag(listov.getIds());
		removeList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isSucceed =blShopListOverview.deleteItem((Ids)v.getTag());
				if(!isSucceed)
					Log.e("adaptor_shoplistOverview", "failed to delete List: " + ((Ids)v.getTag()).getDBId());
				refreshList();
			}
		});
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
