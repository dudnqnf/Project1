package com.takebox.wedding;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;

public class PagerAdapterClass extends PagerAdapter{

	private LayoutInflater mInflater;
	private String images[];
	private Context mContext;
	private String videoYn;
	private String videoUrl;
	
	public PagerAdapterClass(Context c, String [] images){
		super();
		this.images = images;
		mInflater = LayoutInflater.from(c);
		mContext = c;
	}
	
	public PagerAdapterClass(Context c, String [] images, String _videoYn, String _videoUrl){
		super();
		this.images = images;
		this.videoYn = _videoYn;
		this.videoUrl = _videoUrl;
		mInflater = LayoutInflater.from(c);
		mContext = c;
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
		AQuery aq = new AQuery(mContext);
		aq.id(onView).image(images[position], true, true);
		onView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(videoYn!=null){
					if(videoYn.compareTo("Y")==0){
						Intent intent = new Intent(mContext, DetailVideoActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("videoUrl", videoUrl);
						mContext.startActivity(intent);	
					}
				}else{
					Intent intent = new Intent(mContext, ViewPagerActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("IMAGES", images);
					intent.putExtra("INDEX", position);
					mContext.startActivity(intent);
				}
			}
		});

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