/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.takebox.wedding;

import com.takebox.wedding.photoview.PhotoView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.androidquery.AQuery;
import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.R;

/**
 * Lock/Unlock button is added to the ActionBar. Use it to temporarily disable
 * ViewPager navigation in order to correctly interact with ImageView by
 * gestures. Lock/Unlock state of ViewPager is saved and restored on
 * configuration changes.
 * 
 * Julia Zudikova
 */

public class ViewPagerActivity extends Activity {

	private static final String ISLOCKED_ARG = "isLocked";

	private ViewPager mViewPager;
	private MenuItem menuLockItem;

	private static Activity mActivity;

	int index = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		mActivity = ViewPagerActivity.this;

		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		setContentView(mViewPager);

		String[] item = null;

		if (getIntent().getStringExtra("IMAGE") != null) {
			String img = getIntent().getStringExtra("IMAGE");

			item = new String[] { img };

		} else {

			item = getIntent().getStringArrayExtra("IMAGES");
			index = getIntent().getIntExtra("INDEX", 0);

			// item = new
			// String[]{"http://www.vipgold.co.kr/files/2013/12/30/218137868b2c667da5083525bf445d14165640.jpg",
			// "http://cfs2.tistory.com/image/14/tistory/2008/10/08/18/41/48ec8043c51ff"};

		}

		mViewPager.setAdapter(new SamplePagerAdapter(item));
		mViewPager.setCurrentItem(index);

		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
					false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
	}

	static class SamplePagerAdapter extends PagerAdapter {

		// private static int[] sDrawables ;
		// private static Bitmap[] mBitmap;
		//
		private static String[] imgitem;

		@Override
		public int getCount() {
			return imgitem.length;
			// return sDrawables.length;
		}

		public SamplePagerAdapter(String[] item) {
			this.imgitem = item;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());

			// photoView.setImageResource(sDrawables[position]);
			if (imgitem[position].contains("null"))
				photoView.setImageResource(R.drawable.big_img_cdedit_photoarea);
			else {
				AQuery aq = new AQuery(mActivity);
				aq.id(photoView).image(imgitem[position], true, true);
			}

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.viewpager_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// menuLockItem = menu.findItem(R.id.menu_lock);
	// toggleLockBtnTitle();
	// menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
	// @Override
	// public boolean onMenuItemClick(MenuItem item) {
	// toggleViewPagerScrolling();
	// toggleLockBtnTitle();
	// return true;
	// }
	// });
	//
	// return super.onPrepareOptionsMenu(menu);
	// }

	// private void toggleViewPagerScrolling() {
	// if (isViewPagerActive()) {
	// ((HackyViewPager) mViewPager).toggleLock();
	// }
	// }
	//
	// private void toggleLockBtnTitle() {
	// boolean isLocked = false;
	// if (isViewPagerActive()) {
	// isLocked = ((HackyViewPager) mViewPager).isLocked();
	// }
	// String title = (isLocked) ? getString(R.string.menu_unlock) :
	// getString(R.string.menu_lock);
	// if (menuLockItem != null) {
	// menuLockItem.setTitle(title);
	// }
	// }

	private boolean isViewPagerActive() {
		return (mViewPager != null && mViewPager instanceof HackyViewPager);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG,
					((HackyViewPager) mViewPager).isLocked());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
		// analytics 분석도구
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		// analytics 분석도구
		EasyTracker.getInstance(this).activityStop(this); // Add this method.

	}
}
