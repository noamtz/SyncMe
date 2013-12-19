package syncme.app.ui;

import java.util.ArrayList;

import syncme.app.data.DAL;

import syncme.app.model.shoplist.ShopListOverview;
import syncme.app.utils.CommonUtils;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.syncme.R;
import com.google.android.gms.internal.ad;
import com.google.android.gms.internal.bu;

public class FragmentShopList extends Fragment{

	ShopListAdapter adapter;
	ArrayList<ShopListOverview> allListsOverview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		allListsOverview = DAL.getInstance().getShopList().getAllShopListOverview();

		View rootView = inflater.inflate(R.layout.fragment_main_shoplist, container, false);

		ListView lv = (ListView) rootView.findViewById(R.id.shoplist_lv);
		
		adapter = new ShopListAdapter(getActivity(), R.layout.shoplist_list_row , allListsOverview);
		lv.setAdapter(adapter);
		ShopListOverview slo = new ShopListOverview();
		slo.setTitle("Inserted");

		Button btn = (Button) rootView.findViewById(R.id.shoplist_addlist);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShopListOverview slo = new ShopListOverview();
				slo.setTitle("New Shopping List");
				adapter.add(slo);
				adapter.notifyDataSetChanged();
			}
		});

		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				  Intent i = new Intent(getActivity(), ShoppingList.class);
				  Bundle bundle = new Bundle();
				  bundle.putLong("Id", adapter.getItem(position).getId());
				  i.putExtras(bundle);
			      startActivity(i);
			}
		});
		
		return rootView;
	}

	private class ShopListAdapter extends ArrayAdapter<ShopListOverview>{

		private static final String TAG = "ShopListAdapter";

		public ShopListAdapter(Context context, int resource , ArrayList<ShopListOverview> allListsOverview) {
			super(context, resource);
			//addAll(allListsOverview);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {

				LayoutInflater vi;
				vi = LayoutInflater.from(getContext());
				v = vi.inflate(R.layout.shoplist_list_row, null);

			}

			ShopListOverview shopListOverview = getItem(position);
			
			if (shopListOverview != null ) {

				TextView tvtitle = (TextView) v.findViewById(R.id.shoplist_list_tvtitle);
				if (tvtitle != null) { 
					tvtitle.setText(shopListOverview.getId() + ": " + shopListOverview.getTitle());
				}
			}

			return v;
		}

		@Override
		public void add(ShopListOverview object) {
			object.setId(DAL.getInstance().getShopListOverview().create(object.toDB()));
			if(object.getId() != -1){
				super.add(object);
			}else{
				CommonUtils.LogError(TAG, "add", "Failed to create overview in db");
			}
		}
	}

	private class ItemAdapter extends CursorAdapter {

	    private LayoutInflater mLayoutInflater;
	    private Context mContext;
		
		public ItemAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
		     mContext = context;
		     mLayoutInflater = LayoutInflater.from(context); 
		}

		  /**
	     * @author Noam Tzumie
	     * 
	     * @param   v
	     *          The view in which the elements we set up here will be displayed.
	     * 
	     * @param   context
	     *          The running context where this ListView adapter will be active.
	     * 
	     * @param   c
	     *          The Cursor containing the query results we will display.
	     */
		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
