package com.takebox.wedding.dialog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.takebox.wedding.CategoryEditActivity;
import com.takebox.wedding.CategoryEditData;
import com.takebox.wedding.MainActivity;
import com.takebox.wedding.R;


public class AlbumCategoryDialog extends Dialog implements android.view.View.OnClickListener {
	public Context mContext;
	public Button btn_edit;
	
	private ArrayList<String> arrlist;		//현재 앨범들 이름
	private ArrayList<String> arrlist2;		//추천 앨범들 이름
	private ArrayList<String> arrlist3;		//현재 앨범들  cata_seq
	private ArrayList<String> arrlist4;		//추천 맬범들 cata_seq
	public JSONArray mCateList;
	public JSONArray mCateSgtList;
	public String mRoomNo;
	public String mRoomId;
	public String mAuth;
	public static final int EDIT_ALBUM_CATEGORY = 2;
	public static ArrayList<CategoryEditData> myArrlist;

	Activity mActivity;
	SelectCategoryAdapter mAdapter;
	ListView category_list;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.dialog_album_category);
		Log.i("MainActivity.firstExcutePlug", MainActivity.firstExcutePlug+"");
		
		myArrlist = new ArrayList<CategoryEditData>();

		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		btn_edit = (Button)findViewById(R.id.editCategory);
		if(!mAuth.equals("admin"))
			btn_edit.setVisibility(View.INVISIBLE);
		btn_edit.setOnClickListener(this);
		
		String text;
		
		arrlist = new ArrayList<String>();
		arrlist2 = new ArrayList<String>();
		arrlist3 = new ArrayList<String>();
		arrlist4 = new ArrayList<String>();
		
		
		try {
			if(mCateList != null)
			for(int i=0;i<mCateList.length();i++){
				text = mCateList.getJSONObject(i).getString("name");
				arrlist.add(text);
				text = mCateList.getJSONObject(i).getString("cata_seq");
				arrlist3.add(text);
			}
			if(mCateSgtList != null)
			for(int i=0;i<mCateSgtList.length();i++){
				text = mCateSgtList.getJSONObject(i).getString("name");
				arrlist2.add(text);
				text = mCateSgtList.getJSONObject(i).getString("cata_seq");
				arrlist4.add(text);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mCateList != null)
		for(int j = 0; j < arrlist.size(); j++){
	    	myArrlist.add(new CategoryEditData(arrlist.get(j), arrlist3.get(j)));
	    }
		
		if(mCateList != null){
			category_list = (ListView)findViewById(R.id.category_list);
			mAdapter = new SelectCategoryAdapter(mContext, R.layout.edit_category_list, myArrlist);
			category_list.setAdapter(mAdapter);
			
			category_list.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					MainActivity.cate_name = arrlist.get(position);
					MainActivity.cate_seq = arrlist3.get(position);
					MainActivity.firstExcutePlug = false;
					dismiss();
				}
			});
		}
	}	
	

	public AlbumCategoryDialog(Context context, 
			Activity activity, 
			JSONArray cate_list, 
			String room_no, 
			JSONArray cate_sgt_list, 
			String room_id,
			String adminAuth) {
		super(context, android.R.style.Theme_Black_NoTitleBar);
		mActivity = activity;
		mContext = context;		
		mCateList = cate_list;
		mRoomNo = room_no;
		mCateSgtList = cate_sgt_list;
		mRoomId = room_id;
		mAuth = adminAuth;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_edit){
			go_edit();
			dismiss();
		}
	}
	
	public void go_edit() {

		Intent intent = new Intent(mContext, CategoryEditActivity.class);
		intent.putExtra("category_list", arrlist);
		intent.putExtra("cateSgtList", arrlist2);
		intent.putExtra("cata_seq", arrlist3);
		intent.putExtra("cata_sgt_seq", arrlist4);
		intent.putExtra("room_no", mRoomNo);
		intent.putExtra("room_id", mRoomId);
		mActivity.startActivityForResult(intent, EDIT_ALBUM_CATEGORY);
		mActivity.overridePendingTransition(0,0);
	}
	
	
}