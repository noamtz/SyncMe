package app.coupling;


import com.nit.coupling.R;

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
import app.coupling.data.DALShopListOverview;

public class FragmentShopList extends Fragment {

	private AdapterShopListOverview adapter;
	private DALShopListOverview dataSource = DALShopListOverview.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main_shoplist, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.shoplist_lv);
		adapter = new AdapterShopListOverview(this.getActivity(), dataSource.getSource(), true);

		lv.setAdapter(adapter);

		final EditText listName = (EditText) rootView.findViewById(R.id.etListName);

		Button addList = (Button) rootView.findViewById(R.id.btnAddList);


		addList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String title = listName.getText().toString();
				if(!title.isEmpty()){
					dataSource.addList(title);
					refreshList();
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

	private void refreshList(){
		adapter.swapCursor(dataSource.getSource());
		adapter.notifyDataSetChanged();
	}

}
