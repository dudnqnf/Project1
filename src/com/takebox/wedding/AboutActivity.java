package com.takebox.wedding;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author sujong
 * 앱 사용법
 */
public class AboutActivity extends Activity {

	/** Called when the activity is first created. */

	public ViewPager mPager;
	public LinearLayout mPagerPoints;
	public ArrayList<ImageView> pointImgArray;
	int []images = null;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.activity_about);
  		show_image();
  		setImagePoint(images.length);
	}
	
	public void setImagePoint(int count){
		pointImgArray = new ArrayList<ImageView>();
		for(int i = 0; i<count; i++){
			ImageView img = new ImageView(getApplicationContext());
			img.setImageResource(R.drawable.dot_02);
			pointImgArray.add(img);
			mPagerPoints.addView(pointImgArray.get(i));
			pointImgArray.get(i).setPadding(10, 0, 10, 0);
		}
		pointImgArray.get(0).setImageResource(R.drawable.dot_01);
	}
	
	protected void show_image() {
		images = new int[5];
		images[0] = R.drawable.initial_appinfo_start;
		images[1] = R.drawable.initial_appinfo_join_basic;
		images[2] = R.drawable.initial_appinfo_main;
		images[3] = R.drawable.initial_appinfo_invite;
		images[4] = R.drawable.initial_appinfo_invited_list;

		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerPoints = (LinearLayout)findViewById(R.id.point_layout);
		mPager.setAdapter(new PagerAdapterClass(getApplicationContext(), images));
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				for(int i=0; i<pointImgArray.size();i++){
					pointImgArray.get(i).setImageResource(R.drawable.dot_02);
				}
				pointImgArray.get(arg0).setImageResource(R.drawable.dot_01);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	
	private class PagerAdapterClass extends PagerAdapter{

		private LayoutInflater mInflater;
		private int images[];
		public PagerAdapterClass(Context c, int [] images){
			super();
			this.images = images;
			mInflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			
			// TODO Auto-generated method stub
			return images.length;
		}

		@Override
		public Object instantiateItem(View pager, final int position) {
			View v = null;
			v = mInflater.inflate(R.layout.photo_fragment, null);
			ImageView onView = (ImageView)v.findViewById(R.id.photo);
			onView.setImageResource(images[position]);
			onView.setScaleType(ImageView.ScaleType.FIT_XY);
			((ViewPager)pager).addView(v, 0);

			return v; 
		}
		

		@Override
		public void destroyItem(View pager, int position, Object view) {    
			((ViewPager)pager).removeView((View)view);
		}

		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj; 
		}

		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
		@Override public void finishUpdate(View arg0) {}
	}
	
	
	
	
	public void addActivity(){
		IntroActivity.activity.add(this);
	}
	@Override
	public void onStart() {
		super.onStart();
		//analytics 분석도구 
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		//analytics 분석도구 
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.

	}

}