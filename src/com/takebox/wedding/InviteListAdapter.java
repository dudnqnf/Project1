package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.MemberModel;
import com.takebox.wedding.InviteListActivity;

public class InviteListAdapter extends ArrayAdapter<InviteListItem>  {

	private ArrayList<InviteListItem> items;
	private Context mContext;
	private Activity mActivity;
	private String user_auth;		//user_auth = 하객리스트를 보는 이용자의 권한
	private String user_id;
	private String wed_seq;
	private int mPosition;
	private String mU_seq;
	private InviteListItem p;

	static ProgressDialog pd = null;
	private Thread mThread = null;
	
	public InviteListAdapter(Activity activity, int textViewResourceId, ArrayList<InviteListItem> items2, String user_auth, String wed_seq, String user_id) {
		super(activity.getApplicationContext(), textViewResourceId, items2);
		mContext = activity.getApplicationContext();
		mActivity = activity;
		this.items = items2;
		this.user_auth = user_auth;
		this.wed_seq = wed_seq;
		this.user_id = user_id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.invite_list, null);
		}
		


		p = items.get(position);
		final String u_seq = p.u_seq;

		if (p != null) {

			ImageView invite_list_img = (ImageView) v.findViewById(R.id.invite_list_img);
			TextView invite_list_name = (TextView)v.findViewById(R.id.invite_list_name);
			
			ImageView invite_list_add_btn = (ImageView)v.findViewById(R.id.invite_list_add_btn);
			ImageView invite_list_sub_btn = (ImageView)v.findViewById(R.id.invite_list_sub_btn);
			
			//user_auth = 하객리스트를 보는 이용자의 권한,	p.auth = 그 방의 하객들의 권한
			if(p.auth.equals("admin")){
				invite_list_add_btn.setVisibility(View.GONE);
			}
			
			if(!user_auth.equals("admin")){
				invite_list_add_btn.setVisibility(View.GONE);
				invite_list_sub_btn.setVisibility(View.GONE);
			}
			
			if(p.name.equals(user_id)){
				invite_list_sub_btn.setVisibility(View.GONE);
			}
			
			String img_name = "null";
			if(!p.social_img.equals("") && !p.social_img.equals("null")){
				img_name = p.social_img;
			}else if(!p.img.equals("") && !p.img.equals("null")){
				img_name = p.img;
			}
			
			String img_url;
			if(HttpExceptionActivity.sns_img_check(img_name)){
				img_url = img_name;
				AQuery aq = new AQuery(mActivity);
				aq.id(invite_list_img).image(img_url, true, true);
			} else if(!img_name.equals("null")){
				img_url = Info.MASTER_FILE_URL + "/image/"+ img_name;
				AQuery aq = new AQuery(mActivity);
				aq.id(invite_list_img).image(img_url, true, true);
			}

			if(!p.nicname.equals("null") && !p.nicname.equals("")){
				invite_list_name.setText(p.nicname);
			}else{
				invite_list_name.setText(p.name);
			}
			
			
			//추가 삭제 버튼
			invite_list_add_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mPosition = pos;
					mU_seq = u_seq;
					processParsing(mThread, add);
				}
			});
			
			invite_list_sub_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mPosition = pos;
					mU_seq = u_seq;
					processParsing(mThread, delete);
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
		pd.show();
		thread = new Thread(runnable);
		thread.start();
	}
	
	private final Runnable add = new Runnable() {
		@Override
		public void run() {
			add();
		}
	};
	
	private final Runnable delete = new Runnable() {
		@Override
		public void run() {
			delete();
		}
	};
	
	//회원 권한부여
	protected void add() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		
		data.put("wed_seq", wed_seq);
		data.put("u_seq", mU_seq);
		
		JSONObject obj = null;
		if(p.auth.equals("guest")){
			data.put("auth", "admin");
			obj = MemberModel.procUpdateInviteUser(data);
		}
		
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
				handler.sendEmptyMessage(1);
			}else{
				//실패
				handler.sendEmptyMessage(2);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	
	//권한제거 or 탈퇴
	protected void delete() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		
		data.put("wed_seq", wed_seq);
		data.put("u_seq", mU_seq);
		
		JSONObject obj = null;
		if(p.auth.equals("admin")){
			data.put("auth", "guest");
			obj = MemberModel.procUpdateInviteUser(data);
		} else if(p.auth.equals("guest")){
			obj = MemberModel.procDeleteInviteUser(data);
		}
		
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
				Message msg = new Message();
				msg.what = 1;
				handler.sendEmptyMessage(3);
			}else{
				//실패
				handler.sendEmptyMessage(4);
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

				if(pd!=null) pd.cancel(); //등록 성공

				refresh();
				
				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog = AlertDialogBuilder.pop_ok(mContext, "데이터 전송에 실패하였습니다.").create();    // 알림창 객체 생성
				dialog.show();
				
				break;


			
			case 3:
				
				if(pd!=null) pd.cancel(); //등록 성공
				
				refresh();
				
				break;
			case 4:
				if(pd!=null) pd.cancel();
				//등록 실패
	
				AlertDialog dialog2 = AlertDialogBuilder.pop_ok(mContext, "데이터 전송에 실패하였습니다.").create();    // 알림창 객체 생성
				dialog2.show();
				
				break;
				
			}
		}

	};
	
	public void refresh(){
		items.remove(mPosition);
		InviteListActivity.DataRefresh(mActivity, mContext);
//		Toast.makeText(mContext, mPosition+"", Toast.LENGTH_SHORT).show();
	}


}