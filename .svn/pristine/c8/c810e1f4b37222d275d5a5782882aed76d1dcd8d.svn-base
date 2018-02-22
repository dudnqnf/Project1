package com.takebox.wedding;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FacebookFriendList extends Activity {

	ListView friend_list;
	ArrayAdapter<String> mAdapter;
	String [] friend_arr;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_facebook_friendlist);
	    
	    Intent intent = getIntent();
	    String [] name = intent.getStringArrayExtra("name");
	    String [] id = intent.getStringArrayExtra("id");
	    
	    friend_arr = new String[]{"aa", "bb"};
	    friend_list = (ListView)findViewById(R.id.facebook_friend_list);
	    mAdapter = new ArrayAdapter<String>(FacebookFriendList.this, android.R.layout.simple_list_item_1, name);
	    friend_list.setAdapter(mAdapter);
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
