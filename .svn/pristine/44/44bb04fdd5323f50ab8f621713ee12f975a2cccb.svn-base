package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.takebox.wedding.dialog.AlbumShareDialog;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.AlbumModel;
import com.takebox.wedding.util.CustomTextFont;
import com.takebox.wedding.util.Time;

public class ItemAdapter  extends ArrayAdapter<Item> {

	private ProgressDialog pd = null;
	private Thread mThread = null;


	private ArrayList<Item> items;

	private Context mContext;
	private Activity mActivity;

	private String[] imgs;

	private String content_id;
	private int sel_pos;

	private String _like_yn;
	private String adminAuth;
	private String userId;
	private String male_name;
	private String female_name;
	private boolean first_flag = true;

	public ItemAdapter(Activity activity, int textViewResourceId, ArrayList<Item> items, String _adminAuth, String _userId, String male_name, String female_name) {
		super(activity.getApplicationContext(), textViewResourceId, items);
		mContext = activity.getApplicationContext();
		mActivity = activity;
		this.items = items;
		this.adminAuth = _adminAuth;
		this.userId = _userId;
		this.male_name = male_name;
		this.female_name = female_name;

		imgs = new String[items.size()];

		for(int i=0; i<items.size(); i++){
			imgs[i] = items.get(i).img;
		}


	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;


		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_list, null);
		}


		final Item p = items.get(position);


		if (p != null) {

			ImageView iv_img = (ImageView)v.findViewById(R.id.row_img_iv);
			ImageView play_button = (ImageView)v.findViewById(R.id.play_button);
			LinearLayout like_cap = (LinearLayout)v.findViewById(R.id.like_cap);
			LinearLayout reple_cap = (LinearLayout)v.findViewById(R.id.reple_cap);
			final ImageView btn_like = (ImageView)v.findViewById(R.id.row_like_btn);
			ImageView btn_reple = (ImageView)v.findViewById(R.id.row_reple_btn);
			Button btn_share = (Button)v.findViewById(R.id.row_share_btn);
			TextView tv_content = (TextView)v.findViewById(R.id.row_content);
			ImageView row_profile_img = (ImageView)v.findViewById(R.id.row_profile_img);
			TextView row_profile_d = (TextView)v.findViewById(R.id.row_profile_d);
			TextView row_profile_overtime = (TextView)v.findViewById(R.id.row_profile_overtime);
			TextView row_profile_name = (TextView)v.findViewById(R.id.row_profile_name);
			TextView main_like_cnt = (TextView)v.findViewById(R.id.main_like_cnt);
			TextView main_cmt_cnt = (TextView)v.findViewById(R.id.main_cmt_cnt);

			
			if(p.content.length()==0){
				tv_content.setVisibility(View.GONE);
			}else{
				tv_content.setVisibility(View.VISIBLE);
			}
			tv_content.setText(p.content);
			tv_content.setTypeface(CustomTextFont.hunsomsatangR);
			
			//닉네임 설정
			if(!p.in_u_nick.equalsIgnoreCase("null")){
				row_profile_name.setText(p.in_u_nick);
			} else {
				row_profile_name.setText(p.in_u_id);
			}	
			row_profile_name.setTypeface(CustomTextFont.hunsomsatangR);
			
			//게시글 유저 사진
			if(!p.in_u_picture.equalsIgnoreCase("") && !p.in_u_picture.equalsIgnoreCase("null")){
				String img_url = Info.MASTER_FILE_URL + "/image/"+ p.in_u_picture;
				Log.i("p.in_u_picture", p.in_u_picture);
				Log.i("picture", img_url);
				AQuery aq = new AQuery(mContext);
				aq.id(row_profile_img).image(img_url, true, true);
			} else if(!p.in_u_sns_picture.equalsIgnoreCase("") && !p.in_u_sns_picture.equalsIgnoreCase("null")){
				String img_url = p.in_u_sns_picture;
				Log.i("sns_picture", img_url);
				AQuery aq = new AQuery(mContext);
				aq.id(row_profile_img).image(img_url, true, true);
			}
			
			if(p.video_yn.equalsIgnoreCase("Y")){
				play_button.setVisibility(View.VISIBLE);
			}else{
				play_button.setVisibility(View.GONE);
			}
			
				
			//	play_button
			
			
			Time.CalculateOvertime(row_profile_overtime, p.reg_date);
			Time.regdate(row_profile_d, p.reg_date, 1);

			if(!p.img.equals("")){
				//이미지 출력
				AQuery aq =new AQuery(mContext);
				aq.id(iv_img).image(p.img, true, true);
			}
			
			main_like_cnt.setText(p.like_count);
			main_cmt_cnt.setText(p.cmt_cnt);
			
			


			//이미지 클릭 했을때
			iv_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] data = {p.img, p.content, p.con_seq, p.in_u_nick, p.in_u_id, p.in_u_picture, p.in_u_sns_picture, p.reg_date, p.like_count, p.cmt_cnt};

					//앨범 상세로 이동 
					Intent intent = new Intent(mContext, DetailAlbumActivity.class);
					intent.putExtra("DATA", data);
					intent.putExtra("auth", adminAuth);
					intent.putExtra("userId", userId);
					intent.putExtra("male_name", male_name);
					intent.putExtra("female_name", female_name);
					intent.putExtra("position", pos);
					intent.putExtra("focus", "no");
					mActivity.startActivity(intent);


					/**
					//이미지 
					Intent intent = new Intent(mContext, ViewPagerActivity.class);
					intent.putExtra("IMAGES", imgs);
					intent.putExtra("INDEX", pos);
					//intent.putExtra("IMAGE", p.img);
					mActivity.startActivity(intent);
					 */
				}
			});


			/*
			//좋아요 확인 하기
			if(first_flag == true){
				first_flag = false;
				if(p.cancle_yn.equals("Y") || p.cancle_yn.equals("null")){
					btn_like.setImageResource(R.drawable.icon_heart_off);
				}else if(p.cancle_yn.equals("N")){
					btn_like.setImageResource(R.drawable.icon_heart);				
				}
			}
			*/
			//좋아요 확인 하기 
			if(p.cancle_yn.equals("Y") || p.cancle_yn.equals("null")){
				btn_like.setImageResource(R.drawable.icon_heart_off);
			}else if(p.cancle_yn.equals("N")){
				btn_like.setImageResource(R.drawable.icon_heart);				
			}


			mThread = new Thread(like);
			//좋아요 버튼 클릭 
			like_cap.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sel_pos = pos;
					content_id = p.con_seq;
					
//					Drawable heart_btn = btn_like.getDrawable();
//					Drawable icon_heart = mContext.getResources().getDrawable(R.drawable.icon_heart);
//					Drawable icon_heart_off = mContext.getResources().getDrawable(R.drawable.icon_heart_off);
//
//					Bitmap heart_btn_bitmap = ((BitmapDrawable)heart_btn).getBitmap();
//					Bitmap icon_heart_bitmap = ((BitmapDrawable)icon_heart).getBitmap();
//					Bitmap icon_heart_off_bitmap = ((BitmapDrawable)icon_heart_off).getBitmap();
//
//					if(heart_btn_bitmap.equals(icon_heart_bitmap)) {
//						items.get(sel_pos).cancle_yn = "N";
//						btn_like.setImageResource(R.drawable.icon_heart_off);
//						items.get(sel_pos).like_count = (Integer.parseInt(items.get(sel_pos).like_count)-1) + "";
//					}else if(heart_btn_bitmap.equals(icon_heart_off_bitmap)){
//						items.get(sel_pos).cancle_yn = "Y";
//						btn_like.setImageResource(R.drawable.icon_heart);
//						items.get(sel_pos).like_count = (Integer.parseInt(items.get(sel_pos).like_count)+1) + "";
//					}
//					
//					notifyDataSetChanged();
//					
//					if(mThread.isAlive()){
//					} else {
//						mThread.start();
//					}
					
					processParsing(mThread, like);
					
				}
			});



			//댓글 버튼 클릭
			reple_cap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					//댓글 팝업 
//					AlbumRepleDialog pop_reple = new AlbumRepleDialog(mActivity, mActivity, p.con_seq, p.like_count, pos);
//					pop_reple.show();
					
					String[] data = {p.img, p.content, p.con_seq, p.in_u_nick, p.in_u_id, p.in_u_picture, p.in_u_sns_picture, p.reg_date, p.like_count, p.cmt_cnt};

					//앨범 상세로 이동 
					Intent intent = new Intent(mContext, DetailAlbumActivity.class);
					intent.putExtra("DATA", data);
					intent.putExtra("auth", adminAuth);
					intent.putExtra("userId", userId);
					intent.putExtra("male_name", male_name);
					intent.putExtra("female_name", female_name);
					intent.putExtra("position", pos);
					intent.putExtra("focus", "yes");
					mActivity.startActivity(intent);
				}
			});



			//공유하기 버튼 클릭
			btn_share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//공유하기 팝업
					AlbumShareDialog pop_share = new AlbumShareDialog(mActivity, mActivity);
					
					
					pop_share.show();
					
				}
			});



		}
		return v;
	}



	public void processParsing(Thread thread, Runnable runnable) {
		pd = new ProgressDialog(mActivity);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(null);
		pd.setMessage("로드중...");
		//		pd.setButton("���", new DialogInterface.OnClickListener() {
		//			public void onClick(DialogInterface dialog, int which) {
		//				threadStop();
		//				dialog.cancel();
		//			}
		//		});
		pd.show();

		thread = new Thread(runnable);
		thread.start();
	}


	private final Runnable like = new Runnable() {
		@Override
		public void run() {
			like();
		}
	};


	/**
	 * 좋아요 
	 */
	protected void like() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();

		data.put("content_id", content_id);

		JSONObject obj = AlbumModel.procLike(data);
		if(obj==null){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}

		try {
			String info = obj.getString("info");

			//에러코드 확인
			if(info.equals("ok")){
				//성공

				_like_yn = obj.getString("cancle_yn");

				handler.sendEmptyMessage(1);

			}else if(info.equals("fail")){
				//실패
				handler.sendEmptyMessage(2);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 



	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				if(pd!=null) pd.cancel();
				//좋아요 성공
				items.get(sel_pos).cancle_yn = _like_yn;
				Log.i("_like_yn", _like_yn);
				if(_like_yn.equalsIgnoreCase("Y")){
					items.get(sel_pos).like_count = (Integer.parseInt(items.get(sel_pos).like_count)-1) + "";
				} else {
					items.get(sel_pos).like_count = (Integer.parseInt(items.get(sel_pos).like_count)+1) + "";
				}
				notifyDataSetChanged();
				
				break;
				
			case 2:
				if(pd!=null) pd.cancel();
				//좋아요 
				Toast.makeText(mContext, "데이터통신 실패", Toast.LENGTH_SHORT).show();

			}
		}
	};

}
