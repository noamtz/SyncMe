package syncme.app.ui;

import com.example.syncme.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentCalender extends Fragment{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_main_calender, container, false);
  
        
        ((TextView) rootView.findViewById(R.id.tvcalender)).setText(
                getString(R.string.calender));
        return rootView;
    }
}
