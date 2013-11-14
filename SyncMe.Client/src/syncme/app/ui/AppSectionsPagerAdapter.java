package syncme.app.ui;
import com.example.syncme.R;
import syncme.app.logic.Constants;
import syncme.app.utils.CommonUtils;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static syncme.app.logic.Constants.*;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

	public AppSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;
		switch (i) {
		case POSITION_SHOPLIST:
			fragment = new FragmentShopList();
			break;
		case POSITION_CALENDER:
			fragment = new FragmentCalender();
			break;
		case POSITION_TODO:
			fragment = new FragmentTodo();
			break;
		case POSITION_LIBRARY:
			fragment = new FragmentLibrary();
			break;
		}
		 //TODO: Build proper Exceptions
		if(fragment == null)
			fragment = new Fragment();
		return fragment;
	}

	@Override
	public int getCount() {
		return NUM_OF_SCREENS;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		switch (position) {
		case POSITION_SHOPLIST: return CommonUtils.getResourceString(R.string.shoplist);
		case POSITION_CALENDER: return CommonUtils.getResourceString(R.string.calender);
		case POSITION_TODO: return CommonUtils.getResourceString(R.string.todo);
		case POSITION_LIBRARY: return CommonUtils.getResourceString(R.string.library);
		}
		return "";
	}
}