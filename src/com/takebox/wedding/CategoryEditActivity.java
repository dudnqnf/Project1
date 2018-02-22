package com.takebox.wedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.model.WeddingModel;
import com.takebox.wedding.util.CustomTextFont;
import com.takebox.wedding.R;

public class CategoryEditActivity extends HttpExceptionActivity {
	
	private static ArrayList<String> arrlist;		//현재앨범이름
	private static ArrayList<String> arrlist2;		//추천앨범이름
	private static ArrayList<String> arrlist3;		//현재앨범 cata_seq
	private static ArrayList<String> arrlist4;		//추천앨범 cata_seq
	public static ArrayList<CategoryEditData> myArrlist;
	public static ArrayList<CategoryEditData> sgtArrlist;
	private static ProgressDialog pd = null;
	private static Thread mThread = null;
	public String category_name;
	public static String room_no;
	public static JSONArray mCateList;
	public static JSONArray mCateSgtList;
	public static String room_id;
	public static Context cont;
	
	static ListView my_list;
	static ListView common_list;
	EditText album_name;
	static CategoryEditAdapter mMyAdapter;
	static SuggestCategoryAdapter mSgtAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // TODO Auto-generated method stub
	    setContentView(R.layout.activity_category_edit);
	    
	    arrlist = new ArrayList<String>();
	    arrlist2 = new ArrayList<String>();
	    arrlist3 = new ArrayList<String>();
	    arrlist4 = new ArrayList<String>();
	    
	    myArrlist = new ArrayList<CategoryEditData>();	
	    sgtArrlist = new ArrayList<CategoryEditData>();	
	    
	    Intent i =getIntent();
	    
	    room_no = i.getStringExtra("room_no");
	    arrlist = i.getStringArrayListExtra("category_list");
	    arrlist2 = i.getStringArrayListExtra("cateSgtList");
	    arrlist3 = i.getStringArrayListExtra("cata_seq");
	    arrlist4 = i.getStringArrayListExtra("cata_sgt_seq");
	    room_id = i.getStringExtra("room_id");
	    
	    arrlist.remove(arrlist.indexOf("전체"));
	    
//	    Log.i("TAG@!@!@!", arrlist3.size()+"");
//	    Log.i("TAG@!@!@!", arrlist.get(arrlist.size()-1));
//	    Log.i("TAG@!@!@!", arrlist3.get(arrlist.size()-1));
//	    Log.i("TAGing", arrlist2.get(0));
//	    Log.i("TAGffffff", arrlist4.get(0));
	    
	    for(int j = 0; j < arrlist.size(); j++){
	    	myArrlist.add(new CategoryEditData(arrlist.get(j), arrlist3.get(j)));
//	    	Log.i("TAG!!!!", arrlist.get(j));
//	    	Log.i("TAG!!!!", arrlist3.get(j));
	    }	  

	    for(int j = 0; j < arrlist2.size(); j++){
	    	sgtArrlist.add(new CategoryEditData(arrlist2.get(j), arrlist4.get(j)));
//	    	Log.i("TAG!!!!", arrlist2.get(j));
//	    	Log.i("TAG!!!!", arrlist4.get(j));
	    }	
	    
	    
	    
	    album_name = (EditText)findViewById(R.id.album_name);
	    album_name.setTypeface(CustomTextFont.hunsomsatangR);
	    my_list = (ListView)findViewById(R.id.cate_edit_my_list);
	    common_list = (ListView)findViewById(R.id.cate_edit_common_list);
	    
	    mMyAdapter = new CategoryEditAdapter(CategoryEditActivity.this, R.layout.edit_category_list, myArrlist);
		my_list.setAdapter((BaseAdapter) mMyAdapter);
		
		mSgtAdapter = new SuggestCategoryAdapter(CategoryEditActivity.this, R.layout.edit_category_list, sgtArrlist, room_no);
		common_list.setAdapter((BaseAdapter) mSgtAdapter);
		
		Button btn = (Button)findViewById(R.id.add_category);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				category_name = album_name.getText().toString();
				Log.i("category_name", category_name);
				if(category_name != null){
					if(!category_name.equals(""))
						processParsing(mThread, save);
					else
						Toast.makeText(CategoryEditActivity.this, "앨범 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(CategoryEditActivity.this, "앨범 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	public void processParsing(Thread thread, Runnable runnable) {
		pd = new ProgressDialog(CategoryEditActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(null);
		pd.setMessage("로드중...");
		pd.show();

		thread = new Thread(runnable);
		thread.start();
	}
	
	private final Runnable save = new Runnable() {
		@Override
		public void run() {
			save();
		}
	};
	
	private final Runnable load = new Runnable() {
		@Override
		public void run() {
			load();
		}
	};
	
	//앨범 카테고리 등록
	protected void save() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		
		data.put("name", category_name);
		data.put("room_no", room_no);
		data.put("cata_seq", "0");

		JSONObject obj = WeddingModel.procSaveCreateAlbum(data);	

		if(!isHttpWorthCheck(obj)){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}else{
			try {
				if(obj.getString("info").equals("session-out")){
					reLogin();
					return;
				}
			}catch(JSONException e) {
				e.printStackTrace();
			}
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
	
	
	//앨범 카테고리 불러오기
		protected void load() {
			// TODO Auto-generated method stub
			Map<String, String> data = new HashMap<String,String>();
			
			data.put("room_id", room_id);

			JSONObject obj = WeddingModel.procGetWeddingRoom(data);	
			if(!isHttpWorthCheck(obj)){
				if(pd.isShowing())
					pd.dismiss();
				return;
			}else{
				try {
					if(obj.getString("info").equals("session-out")){
						reLogin();
						return;
					}
				}catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			try {
				mCateList = new JSONArray(obj.getString("cateList"));
				mCateSgtList = new JSONArray(obj.getString("cateSgtList"));
				String info = obj.getString("auth");

				//에러코드 확인
				if(info.equals("admin")){
					//성공
					handler.sendEmptyMessage(3);
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
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				if(pd!=null) pd.cancel(); //등록 성공

				add_category();
				((BaseAdapter) mMyAdapter).notifyDataSetChanged();
				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog = AlertDialogBuilder.pop_ok(CategoryEditActivity.this, "데이터 전송에 실패하였습니다.").create();    // 알림창 객체 생성
				dialog.show();
				
				break;
			
			case 3:
				if(pd!=null) pd.cancel();
				//등록 실패
				loadData();
				
				break;
			}
	
		}

	};
	
	public void add_category(){
		processParsing(mThread, load);
//		Toast.makeText(AlbumCategoryEditActivity.this, "통신완료", Toast.LENGTH_SHORT).show();
	}

	
	public void loadData(){
//		Log.i("TAG", "여기까지는 실행됨");
		ArrayList<CategoryEditData> list = new ArrayList<CategoryEditData>();
		ArrayList<CategoryEditData> list2 = new ArrayList<CategoryEditData>();
		try {
			String text;
			arrlist.clear();
			arrlist2.clear();
			arrlist3.clear();
			arrlist4.clear();
			for(int i=0;i<mCateList.length();i++){
				text = mCateList.getJSONObject(i).getString("name");
				if(!text.equals("전체")){
					arrlist.add(text);
					text = mCateList.getJSONObject(i).getString("cata_seq");
					arrlist3.add(text);
				}
			}
			for(int i=0;i<mCateSgtList.length();i++){
				text = mCateSgtList.getJSONObject(i).getString("name");
				arrlist2.add(text);
				text = mCateSgtList.getJSONObject(i).getString("cata_seq");
				arrlist4.add(text);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		for(int j = 0; j < arrlist.size(); j++){
			list.add(new CategoryEditData(arrlist.get(j), arrlist3.get(j)));
	    }
		for(int j = 0; j < arrlist2.size(); j++){
			list2.add(new CategoryEditData(arrlist2.get(j), arrlist4.get(j)));
	    }
		
		mMyAdapter = new CategoryEditAdapter(CategoryEditActivity.this, R.layout.edit_category_list, list);
		my_list.setAdapter(mMyAdapter);
//		mSgtAdapter = new SuggestCategoryAdapter(AlbumCategoryEditActivity.this, R.layout.edit_category_list, sgtArrlist, room_no);
//		common_list.setAdapter((ListAdapter) mSgtAdapter);
		
		MainActivity.firstExcutePlug = false;
	}

	public static void loadData2(Context mContext) {
		cont = mContext;
		processParsing2(mThread, load2);
	}
	
	public static void processParsing2(Thread thread, Runnable runnable) {
		pd = new ProgressDialog(cont);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(null);
		pd.setMessage("로드중...");
		pd.show();

		thread = new Thread(runnable);
		thread.start();
	}
	
	private final static Runnable load2 = new Runnable() {
		@Override
		public void run() {
			load2();
		}
	};
	
	//앨범 카테고리 불러오기
	protected static void load2() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();
		
		data.put("room_id", room_id);

		JSONObject obj = WeddingModel.procGetWeddingRoom(data);	
		
		try {
			mCateList = new JSONArray(obj.getString("cateList"));
			mCateSgtList = new JSONArray(obj.getString("cateSgtList"));
			String info = obj.getString("auth");

			//에러코드 확인
			if(info.equals("admin")){
				//성공
				handler2.sendEmptyMessage(3);
			}else{
				//실패
				handler2.sendEmptyMessage(2);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	
	private final static Handler handler2 = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog = AlertDialogBuilder.pop_ok(cont, "데이터 전송에 실패하였습니다.").create();    // 알림창 객체 생성
				dialog.show();
				
				break;
			
			case 3:
				if(pd!=null) pd.cancel();
				//등록 실패
				loadData2();
				
				break;
			}
	
		}

	};
	
	public static void loadData2(){
//		Log.i("TAG", "여기까지는 실행됨");
		ArrayList<CategoryEditData> list = new ArrayList<CategoryEditData>();
		ArrayList<CategoryEditData> list2 = new ArrayList<CategoryEditData>();
		try {
			String text;
			arrlist.clear();
			arrlist2.clear();
			arrlist3.clear();
			arrlist4.clear();
			for(int i=0;i<mCateList.length();i++){
				text = mCateList.getJSONObject(i).getString("name");
				if(!text.equals("전체")){
					arrlist.add(text);
					text = mCateList.getJSONObject(i).getString("cata_seq");
					arrlist3.add(text);
				}
			}
			for(int i=0;i<mCateSgtList.length();i++){
				text = mCateSgtList.getJSONObject(i).getString("name");
				arrlist2.add(text);
				text = mCateSgtList.getJSONObject(i).getString("cata_seq");
				arrlist4.add(text);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		for(int j = 0; j < arrlist.size(); j++){
			list.add(new CategoryEditData(arrlist.get(j), arrlist3.get(j)));
	    }
		for(int j = 0; j < arrlist2.size(); j++){
			list2.add(new CategoryEditData(arrlist2.get(j), arrlist4.get(j)));
	    }
		
		mMyAdapter = new CategoryEditAdapter(cont, R.layout.edit_category_list, list);
		my_list.setAdapter(mMyAdapter);
		mSgtAdapter = new SuggestCategoryAdapter(cont, R.layout.edit_category_list, list2, room_no);
		common_list.setAdapter(mSgtAdapter);
		
		MainActivity.firstExcutePlug = false;
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



