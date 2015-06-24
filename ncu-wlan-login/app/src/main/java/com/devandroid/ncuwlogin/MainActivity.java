package com.devandroid.ncuwlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.devandroid.ncuwlogin.callbacks.Constant;

public class MainActivity extends AppCompatActivity {

	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViews();
		setUpViews();
	}

	private void findViews() {
		mViewPager = (ViewPager) findViewById(R.id.pager_main);
	}

	private void setUpViews() {
		MainPagerAdapter mQLPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mQLPagerAdapter);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_main);
		tabLayout.setTabMode(TabLayout.MODE_FIXED);
		tabLayout.setTabsFromPagerAdapter(mQLPagerAdapter);
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.about:
				startActivity(new Intent(this, AboutActivity.class));
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private class MainPagerAdapter extends FragmentPagerAdapter {

		public MainPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		public String getFragmentTag(int position) {
			return "android:switcher:" + R.id.pager_main + ":" + position;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return NCUWLFragment.newInstance();
				case 1:
					return NCUCSIEFragment.newInstance();
				default:
					Log.e(Constant.TAG, "Unexpected ViewPager item position: " + position);
					return new Fragment();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "NCUWL";
				case 1:
					return "NCU-CSIE";
				default:
					return "?";
			}
		}
	}
}
