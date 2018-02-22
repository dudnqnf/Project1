package com.takebox.wedding;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.R;

class WeddingListAdapter extends BaseAdapter{
	Context mContext;
	LayoutInflater Inflater;
	static ArrayList<WeddingListItem> arrlist = new ArrayList<WeddingListItem>();
	int mLayout;
	
	
	public WeddingListAdapter(Context context, int layout, ArrayList<WeddingListItem> wedding_list_item){
		mContext = context;
		Inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		arrlist = wedding_list_item;
		mLayout = layout;
	}
	
	@Override
	public int getCount() {
		return arrlist.size();
	}

	@Override
	public WeddingListItem getItem(int position) {		
		return arrlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		if(convertView == null) {
			convertView = Inflater.inflate(mLayout, parent, false);
		}
		
		String room_id = arrlist.get(position).room_id;
		
		TextView room_id_text = (TextView)convertView.findViewById(R.id.wedding_list_id);
		room_id_text.setText(room_id);
		
		
		ImageView profile_img = (ImageView)convertView.findViewById(R.id.wedding_list_img);
		String room_img = arrlist.get(position).room_img;
		if(!room_img.equalsIgnoreCase("null") && !room_img.equalsIgnoreCase("")){
			String img_name = room_img;
			String img_url = Info.MASTER_FILE_URL + "/image/"+ img_name;
			AQuery aq = new AQuery(mContext);
			aq.id(profile_img).image(img_url, true, true);
		}
		
		return convertView;
	}
	
}