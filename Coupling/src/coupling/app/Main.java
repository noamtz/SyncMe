package coupling.app;

import com.nit.coupling.R;

import coupling.app.ui.AppSectionsPagerAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

/**
 * This is the Main activity of the application 
 * it holds pager view with 2 fragments (at the moment)
 * 
 * @author Noam Tzumie
 *
 */
public class Main extends FragmentActivity implements ActionBar.TabListener{

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main);
		
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(onPageSelected(actionBar));
		
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {

			actionBar.addTab(
					actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * Handle the navigation between the fragment
	 * @param actionBar
	 * @return
	 */
	private ViewPager.SimpleOnPageChangeListener onPageSelected(final ActionBar actionBar){
		return new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		};
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}


}
