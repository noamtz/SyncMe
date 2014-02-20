package coupling.app.ui;

import com.nit.coupling.R;

import coupling.app.Utils;
import coupling.app.BL.BLCalendarEvents;
import coupling.app.models.CalenderEvent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Cursor adaptor between calendar events and list view
 * @author ibental
 *
 */
public class CalendarEventsListAdapter extends CursorAdapter {

	private LayoutInflater mLayoutInflater;
	private BLCalendarEvents blCalendarEvents;
	
	public CalendarEventsListAdapter(Context context, BLCalendarEvents blCalendarEvents, String date) {
		super(context, blCalendarEvents.getDayEvents(date), true);
		mLayoutInflater = LayoutInflater.from(context);
		this.blCalendarEvents = blCalendarEvents;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		
		CalenderEvent event = new CalenderEvent(cursor);
		
		TextView title = (TextView) row.findViewById(R.id.list_event_title);
		TextView startTime = (TextView) row.findViewById(R.id.list_event_start_time);
		TextView endTime = (TextView) row.findViewById(R.id.list_event_end_time);
		
		title.setText(event.getTitle());
		startTime.setText(event.getStartTime());
		endTime.setText(event.getEndTime());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.event_row, parent, false);
	}
	
	public void refresh(String date){
		Cursor c = blCalendarEvents.getDayEvents(date);
		Utils.Log("CalendarEventsListAdapter", "refresh","Size cursor: " + c.getCount());
		swapCursor(c);
		notifyDataSetChanged();
	}

}
