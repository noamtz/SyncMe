package coupling.app;

import com.google.android.gms.internal.ad;
import com.nit.coupling.R;

import coupling.app.BL.BLShopListOverview;
import coupling.app.com.IBLConnector;
import coupling.app.data.DALShopListOverview;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FragmentShopList extends Fragment implements IBLConnector{

	private AdapterShopListOverview adapter;
	
	private BLShopListOverview blShopListOverview;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main_shoplist, container, false);
		blShopListOverview = new BLShopListOverview();
		
		ListView lv = (ListView) rootView.findViewById(R.id.shoplist_lv);
		adapter = new AdapterShopListOverview(this.getActivity(), blShopListOverview);
		
		lv.setAdapter(adapter);

		final EditText listName = (EditText) rootView.findViewById(R.id.etListName);

		Button addList = (Button) rootView.findViewById(R.id.btnAddList);


		addList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String title = listName.getText().toString();
				if(!title.isEmpty()){
					blShopListOverview.createList(title);
					adapter.refreshList();
				}
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				
				Cursor cursor = (Cursor) adapter.getItem(position);
				
				Intent i = new Intent(getActivity(), ShopList.class);
				Bundle bundle = new Bundle();
				
				bundle.putString("Title", cursor.getString(cursor.getColumnIndexOrThrow("Title")));
				bundle.putLong("Id", id);
				
				Log.v("fragment_shoplist", "id: " + id + " , title: " + cursor.getString(cursor.getColumnIndexOrThrow("Title")));
				
				i.putExtras(bundle);
				startActivity(i);
			}
		});

		return rootView;
	}



	@Override
	public void onPause() {
		super.onPause();
		blShopListOverview.unsetBLConnector();
	}



	@Override
	public void onResume() {
		super.onResume();
		blShopListOverview.setBLConnector(this);
	}



	@Override
	public void Refresh() {
		adapter.refreshList();
	}
}
