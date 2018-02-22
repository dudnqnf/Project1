package com.takebox.wedding.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.takebox.wedding.DetailAlbumActivity;
import com.takebox.wedding.HttpExceptionActivity;
import com.takebox.wedding.MainActivity;
import com.takebox.wedding.RepleItem;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.model.AlbumModel;
import com.takebox.wedding.util.Time;
import com.takebox.wedding.R;

public class AlbumRepleDialog extends Dialog implements android.view.View.OnClickListener {
	public Context mContext;

	Activity mActivity;


	private ProgressDialog pd = null;
	private Thread mThread = null;

	private ArrayList<RepleItem> items;
	private ListView lv_list;
	private RepleItemAdapter adapter;

	
	private String con_seq;
	private String like_count;
	private int position;
	
	private JSONArray reple_array;
	
	private Boolean scoll_flag = false;
	
	
	public String getCon_seq() {
		return con_seq;
	}



	public void setCon_seq(String con_seq) {
		this.con_seq = con_seq;
	}


	




	public AlbumRepleDialog(Context context, Activity activity, String _con_seq, String _like_count, int pos) {
		super(context, android.R.style.Theme_Black_NoTitleBar);

		mActivity = activity;
		mContext = context;
		con_seq = _con_seq;
		like_count = _like_count;
		position = pos;
		
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.dialog_album_reple);

		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



		lv_list = (ListView)findViewById(R.id.pop_list);
		TextView tv_like_count = (TextView)findViewById(R.id.like_count);
		tv_like_count.setText(like_count+"명이 좋아합니다.");


		//리플 가져오기
		processParsing(mThread, get_reple_list);
		
		
		Button btn_ok = (Button)findViewById(R.id.pop_reple_ok_btn);
		btn_ok.setOnClickListener(this);

	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.pop_reple_ok_btn:
			
			EditText et_reple = (EditText)findViewById(R.id.pop_reple_input_et);
			String comment =  et_reple.getText().toString();
			
			if(comment.isEmpty()){
				
				AlertDialog dialog = AlertDialogBuilder.pop_ok(mActivity, "내용을 입력해 주세요.").create();
				dialog.show(); 
				return;
			}
			
			//댓글쓰기 버튼 
			MainActivity.editedComment = "add";
			MainActivity.editedListPos = position;
			processParsing(mThread, write_reple);
			
			
			break;

		default:
			break;
		}

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


	private final Runnable get_reple_list = new Runnable() {
		@Override
		public void run() {
			get_reple_list();
		}
	};
	
	private final Runnable write_reple = new Runnable() {
		@Override
		public void run() {
			write_reple();
		}
	};

	/**
	 * 댓글 작성
	 */
	protected void write_reple() {
		
		EditText et_reple = (EditText)findViewById(R.id.pop_reple_input_et);
		String comment =  et_reple.getText().toString();
		
		Map<String, String> data = new HashMap<String,String>();
		data.put("con_seq", con_seq);
		data.put("comment", comment);

		JSONObject obj = AlbumModel.procWriteReple(data);
		if(obj==null){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}
		
		
		
		
		try {
			
			reple_array = new JSONArray(obj.getString("cmtList"));
			
				//성공
				handler.sendEmptyMessage(5);
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}


	/**
	 * 댓글 리스트를 가져온다
	 */
	protected void get_reple_list() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();

		data.put("con_seq", con_seq);

		JSONObject obj = AlbumModel.procGetRepleList(data);


		try {
			reple_array = new JSONArray(obj.getString("cmtList"));
			
			//성공
			handler.sendEmptyMessage(1);
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
				//댓글 리스트 가져오기 성공
				show_list();

				break;
				
			case 5:
				if(pd!=null) pd.cancel();
				//댓글 작성 성공
				
				EditText et_reple = (EditText)findViewById(R.id.pop_reple_input_et);
				et_reple.setText("");
				
				InputMethodManager imm= (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_reple.getWindowToken(), 0);
				
				show_list();
				
				break;
				
			case 6:
				if(pd!=null) pd.cancel();
				//댓글 작성 실패
				break;

			}
		}
	};


	public class RepleItemAdapter extends ArrayAdapter<RepleItem>  {

		private ArrayList<RepleItem> items;

		public RepleItemAdapter(Context context, int textViewResourceId,
				ArrayList<RepleItem> items) {
			super(context, textViewResourceId, items);

			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_reple, null);
			}


			final RepleItem p = items.get(position);

			if (p != null) {
				TextView tv_name = (TextView) v.findViewById(R.id.row_name);
				TextView tv_date = (TextView)v.findViewById(R.id.row_date);
				TextView tv_content = (TextView)v.findViewById(R.id.row_content);
				ImageView iv_img = (ImageView)v.findViewById(R.id.row_reple_profile_img);
				
				String img_name = "null";
				if(!p.social_img.equals("") && !p.social_img.equals("null")){
					img_name = p.social_img;
				}else if(!p.img.equals("") && !p.img.equals("null")){
					img_name = p.img;
				}
				
				String img_url = Info.DEFAULT_IMG;
				if(HttpExceptionActivity.sns_img_check(img_name)){
					img_url = img_name;
				} else {
					img_url = Info.MASTER_FILE_URL + "/image/"+ img_name;
				}
				if(!img_name.equals("null") && !img_name.equals("")){
					AQuery aq = new AQuery(mActivity);
					aq.id(iv_img).image(img_url, true, true);
				}
				
				if(p.nicname.equals("null") && p.nicname.equals("")){
					tv_name.setText(p.name);
				} else {
					tv_name.setText(p.nicname);
				}

					
//				tv_date.setText(p.date);
				Time.CalculateOvertime(tv_date, 20+p.date);
				
				tv_content.setText(p.content);
				
			}
			return v;
		}




	}




	/**
	 * 댓글 리스트 보여주기
	 */
	protected void show_list() {
		// TODO Auto-generated method stub
		
		
		items = new ArrayList<RepleItem>();
		
		
		
		int list_cnt = reple_array.length();
		
		for(int i = 0 ; i < list_cnt ; i++){

			try {
				JSONObject obj = reple_array.getJSONObject(i);
				
				RepleItem item = new RepleItem();

				item.name = obj.getString("cmt_u_id");
				item.date = obj.getString("reg_date");
				item.content = obj.getString("content");
				item.nicname = obj.getString("cmt_u_nick");
				item.img = obj.getString("cmt_u_picture");
				item.social_img = obj.getString("cmt_u_sns_picture");

				items.add(item);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		adapter = new RepleItemAdapter(mContext, R.layout.row_reple, items);
		lv_list.setAdapter(adapter);
		
		
		//처음엔 스크롤을 맨위에 보여주고 다음부턴 아래로 이동 시킨다.
		if(scoll_flag){
			lv_list.setSelection(list_cnt);		
		}
		scoll_flag = true;
		
		
	}



}