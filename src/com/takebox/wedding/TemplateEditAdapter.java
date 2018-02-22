package com.takebox.wedding;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

public class TemplateEditAdapter extends PagerAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<TemplateItem> mItems;
	private String post_card_type;
	private ArrayList<String> buy_list;
	
	ImageView template_box_radio1;
	ImageView template_box_radio2;
	ImageView template_box_radio3;
	
	public TemplateEditAdapter(Context context, ArrayList<TemplateItem> items, ArrayList<String> buy_list, String post_card_type){
		super();
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mItems = items;
		this.post_card_type = post_card_type;
		this.buy_list = buy_list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public View instantiateItem(View pager, final int position) {
		View view = mInflater.inflate(R.layout.template, null);
		ImageView template_img1 = (ImageView)view.findViewById(R.id.template_img1);
		ImageView template_img2 = (ImageView)view.findViewById(R.id.template_img2);
		ImageView template_img3 = (ImageView)view.findViewById(R.id.template_img3);
		
		AQuery aq = new AQuery(mContext);
		aq.id(template_img1).image(mItems.get(position).save_path1, true, true);
		aq = new AQuery(mContext);
		aq.id(template_img2).image(mItems.get(position).save_path2, true, true);
		aq = new AQuery(mContext);
		aq.id(template_img3).image(mItems.get(position).save_path3, true, true);
		
		TextView template_title1 = (TextView)view.findViewById(R.id.template_title1);
		TextView template_title2 = (TextView)view.findViewById(R.id.template_title2);
		TextView template_title3 = (TextView)view.findViewById(R.id.template_title3);
		String view_type1 = mItems.get(position).view_type1;
		String view_type2 = mItems.get(position).view_type2;
		String view_type3 = mItems.get(position).view_type3;
		template_title1.setText(view_type1);
		template_title2.setText(view_type2);
		template_title3.setText(view_type3);
		
		LinearLayout template01 = (LinearLayout)view.findViewById(R.id.template01);
		LinearLayout template02 = (LinearLayout)view.findViewById(R.id.template02);
		LinearLayout template03 = (LinearLayout)view.findViewById(R.id.template03);
		
		template_box_radio1 = (ImageView)view.findViewById(R.id.template_box_radio1);
		template_box_radio2 = (ImageView)view.findViewById(R.id.template_box_radio2);
		template_box_radio3 = (ImageView)view.findViewById(R.id.template_box_radio3);
		template_box_radio1.setImageResource(mItems.get(position).btn_radio1);
		template_box_radio2.setImageResource(mItems.get(position).btn_radio2);
		template_box_radio3.setImageResource(mItems.get(position).btn_radio3);
		
		if(mItems.get(position).type1.equals(post_card_type)){
			template_box_radio1.setImageResource(R.drawable.btn_radio_on);
			post_card_type = "";
		}
		if(mItems.get(position).type2.equals(post_card_type)){
			template_box_radio2.setImageResource(R.drawable.btn_radio_on);
			post_card_type = "";
		}
		if(mItems.get(position).type3.equals(post_card_type)){
			template_box_radio3.setImageResource(R.drawable.btn_radio_on);
			post_card_type = "";
		}
			
		
		template01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TemplateEditActivity.refresh(mContext, position, 1);
			}
		});
		template02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TemplateEditActivity.refresh(mContext, position, 2);
			}
		});
		template03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TemplateEditActivity.refresh(mContext, position, 3);
			}
		});
		
		TextView template_box_cash1 = (TextView)view.findViewById(R.id.template_box_cash1);
		if(mItems.get(position).cash1.equals("0"))
			mItems.get(position).cash1 = "무료";
		else if(buy_list.contains(mItems.get(position).type1))
			mItems.get(position).cash1 = "구매완료";
		else if(mItems.get(position).cash1 != "" && !mItems.get(position).cash1.contains("￦") && !mItems.get(position).cash1.contains("무료"))
			mItems.get(position).cash1 = mItems.get(position).cash1 + " ￦";
		template_box_cash1.setText(mItems.get(position).cash1);
		
		TextView template_box_cash2 = (TextView)view.findViewById(R.id.template_box_cash2);
		if(mItems.get(position).cash2.equals("0"))
			mItems.get(position).cash2 = "무료";
		else if(buy_list.contains(mItems.get(position).type2))
			mItems.get(position).cash2 = "구매완료";
		else if(mItems.get(position).cash2 != "" && !mItems.get(position).cash2.contains("￦") && !mItems.get(position).cash2.contains("무료"))
			mItems.get(position).cash2 = mItems.get(position).cash2 + " ￦";
		template_box_cash2.setText(mItems.get(position).cash2);
		
		TextView template_box_cash3 = (TextView)view.findViewById(R.id.template_box_cash3);
		if(mItems.get(position).cash3.equals("0"))
			mItems.get(position).cash3 = "무료";
		else if(buy_list.contains(mItems.get(position).type3))
			mItems.get(position).cash3 = "구매완료";
		else if(mItems.get(position).cash3 != "" && !mItems.get(position).cash3.contains("￦") && !mItems.get(position).cash3.contains("무료"))
			mItems.get(position).cash3 = mItems.get(position).cash3 + " ￦";
		template_box_cash3.setText(mItems.get(position).cash3);
		
		((ViewPager)pager).addView(view, 0);

		return view;
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
	
	@Override
	public int getItemPosition(Object object){
	     return POSITION_NONE;
	}
}
