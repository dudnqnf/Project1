package com.takebox.wedding;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.StatusCallback;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.info.Info;

public class FacebookActivity extends Activity {
	
	private Button sendRequestButton;
	private UiLifecycleHelper uiHelper;
	public String room_no="0";
	public String room_id="";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook);
		
		//페이스북 세션 설정
		facebook_init(savedInstanceState);
		callRequestBatch();
		
		uiHelper = new UiLifecycleHelper(FacebookActivity.this, callback);
	    uiHelper.onCreate(savedInstanceState);
	    
	    Intent intent = getIntent();
	    if(intent.getStringExtra("room_id") != null)
	    	if(!intent.getStringExtra("room_id").equals("")){
			    room_no = intent.getStringExtra("room_no");
			    room_id = intent.getStringExtra("room_id");
	    	}
		
		LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
//		authButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
		authButton.setPublishPermissions(Arrays.asList("publish_actions"));
	    
	    sendRequestButton = (Button)findViewById(R.id.sendRequestButton);
	    sendRequestButton.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            sendRequestDialog();        
	        }
	    });
	}
	
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	private class SessionStatusCallback implements Session.StatusCallback {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	            // Respond to session state changes, ex: updating the view
	    	callRequestBatch();
	    }
	}
	
	private void facebookLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
			.setCallback(new StatusCallback() {

				@Override
				public void call(Session session, SessionState state, Exception exception) {
					// TODO Auto-generated method stub
					if(session.isOpened()){
						callRequestBatch();
					}

				}
			})
			.setPermissions(Arrays.asList("email","public_profile","user_friends")));
		} else {
//			NewPermissionsRequest permission = new NewPermissionsRequest(FacebookActivity.this, Arrays.asList("email","public_profile","user_friends"));
//			Session.getActiveSession().requestNewReadPermissions(permission);
			Session.openActiveSession(this, true, 
					Arrays.asList("email","public_profile","user_friends"), 
					new StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					session.refreshPermissions();
					// TODO Auto-generated method stub
					if(session.isOpened()){
						callRequestBatch();
					}
				}
			});
			
		}
	}
	
	private void sendRequestDialog() {
		Bundle params = new Bundle();
	    params.putString("message", "WEDDINGBOX");
	    params.putString("title", "초대,추천하기");
	    params.putString("data", "{\"room_no\":"+room_no+"," + "\"room_id\":"+room_id+"}");
	    params.putString("redirect_uri", Info.PC_MARKET_URL);

	    WebDialog requestsDialog = (
	        new WebDialog.RequestsDialogBuilder(FacebookActivity.this,
	            Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                    FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(FacebookActivity.this.getApplicationContext(), 
	                                "취소되었습니다.", 
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(FacebookActivity.this.getApplicationContext(), 
	                                "네트워크 오류", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(FacebookActivity.this.getApplicationContext(), 
	                                "앱요청이 완료되었습니다.",  
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(FacebookActivity.this.getApplicationContext(), 
	                                "취소되었습니다.", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    }   
	                    finish();
	                }
	            })
	            .build();
	    requestsDialog.show();
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i("TAG", "Logged in...");
	    } else if (state.isClosed()) {
	    	session.closeAndClearTokenInformation();
	        Log.i("TAG", "Logged out...!!");
	        finish();
	    }
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}
	
	private void facebook_init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, new StatusCallback() {

					@Override
					public void call(Session session, SessionState state, Exception exception) {
						// TODO Auto-generated method stub
					}
				}, savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
				
				.setCallback(new StatusCallback() {

					@Override
					public void call(Session session, SessionState state, Exception exception) {
						// TODO Auto-generated method stub
						if(session.isOpened()){
						}

					}
				})
				.setPermissions(Arrays.asList("email","public_profile","user_friends")));
			}
		}
	}
	
	private void callRequestBatch(){
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session.isOpened()) {

			//페이스북 로그인 상태입니다.
			RequestBatch requestBatch = new RequestBatch();
			requestBatch
			.add(new Request(Session.getActiveSession(), 
					"me", null, null, new Request.Callback() {
				public void onCompleted(Response response) {
					sendRequestDialog();
				}
			}));
			
			requestBatch.executeAsync();
			
		} else {
			//페이스북 로그인 상태가 아닙니다.
			facebookLogin();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
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
