package coupling.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;


import com.nit.coupling.R;

import coupling.app.Utils;
import coupling.app.BL.BLCalendarEvents;
import coupling.app.BL.BLFactory;
import coupling.app.com.IBLConnector;
import coupling.app.data.Constants;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentCalendar extends Fragment implements IBLConnector{

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
	// marker.
	public ArrayList<String> items; // container to store calendar items which
	// needs showing the event marker
	ArrayList<String> event;
	LinearLayout rLayout;
	ArrayList<String> date;
	ArrayList<String> desc;
	
	
	BLCalendarEvents blCalendarEvents;
	View rootView;
	Activity activity;
	ListView eventList;
	
	String selectedDate;
	
	CalendarEventsListAdapter listAdapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.calendar, container, false);
		blCalendarEvents = BLFactory.getInstance().getCalendarEvents();

		activity = this.getActivity();
		
		Locale.setDefault(Locale.US);

		rLayout = (LinearLayout) rootView.findViewById(R.id.text);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<String>();

		adapter = new CalendarAdapter(activity, month);

		GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		//handler.post(calendarUpdater);

		TextView title = (TextView) rootView.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		
		ListView eventList = (ListView) rootView.findViewById(R.id.events_list);
		listAdapter = new CalendarEventsListAdapter(this.getActivity(), blCalendarEvents);
		eventList.setAdapter(listAdapter);

		RelativeLayout previous = (RelativeLayout) rootView.findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) rootView.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// removing the previous view if added
				if (((LinearLayout) rLayout).getChildCount() > 0) {
					((LinearLayout) rLayout).removeAllViews();
				}
				desc = new ArrayList<String>();
				date = new ArrayList<String>();
				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				selectedDate = CalendarAdapter.dayString
						.get(position);
				Log.w("DEBUG", selectedDate.toString());
				String[] separatedTime = selectedDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				
				  //////////////////////////
				 //get Event List from DB//
				
//				blCalendarEvents.
				/////////////////////////

				/*
				for (int i = 0; i < Utils.events.size(); i++) {
					if (Utils.events.get(i).getstartTime().equals(selectedGridDate)) {
						desc.add(Utils.events.get(i).getTitle());
					}
				}
				*/
				

				if (desc.size() > 0) {
					for (int i = 0; i < desc.size(); i++) {
						TextView rowTextView = new TextView(activity);

						// set some properties of rowTextView or something
						rowTextView.setText("Event:" + desc.get(i));
						rowTextView.setTextColor(Color.BLACK);

						// add the textview to the linearlayout
						rLayout.addView(rowTextView);

					}

				}

				desc = null;

			}

		});

		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		selectedDate = df.format(GregorianCalendar.getInstance().getTime());
		Utils.Log("Calendar", selectedDate);

		setHasOptionsMenu(true);
		return rootView;
	}
	
	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}

	}



	public void refreshCalendar() {
		TextView title = (TextView) rootView.findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		//handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	/*
	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;
			event = Utils.readCalendarEvent(activity);
			Log.d("=====Event====", event.toString());
			Log.d("=====Date ARRAY====", Utils.startDates.toString());

			for (int i = 0; i < Utils.startDates.size(); i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add(Utils.startDates.get(i).toString());
			}
			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};
	*/
	

	@Override
	public void Refresh() {

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.calendar_actionbar, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_event_action:
			Intent inviteIntent = new Intent(getActivity(), CalendarEventActivity.class);
			
			inviteIntent.putExtra(Constants.SELECTED_DATE, selectedDate);
			inviteIntent.putExtra(Constants.EVENT_ID, Constants.EVENT_CREATE);
			
			startActivity(inviteIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
