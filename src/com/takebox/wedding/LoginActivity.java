package com.takebox.wedding;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.kakao.SessionCallback;
import com.kakao.UserProfile;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.info.User;
import com.takebox.wedding.model.MemberModel;
import com.takebox.wedding.util.CommonUtil;
import com.takebox.wedding.util.CustomTextFont;

public class LoginActivity extends HttpExceptionActivity implements 
	ConnectionCallbacks, OnConnectionFailedListener{


	private EditText et_email;
	private EditText et_password;

	private Thread mThread = null;

	private String GCM_CODE = "";

	private JSONObject fb_obj;

	private String sns_flag= ""; // 가입유형 구분 (fb/ tw/ gg)
	private String sns_id;
	private String _id = ""; //로그인 아이디
	private String nick_name;


	private ImageView btn_facebook;
	private Boolean fb_login_click = false;
	private String fb_access_token;
	private Boolean inter_flag  = true;
	private int facebook_flag = 0;	//페이스북으로 가입한적이 있는 상태이면 웨딩룸으로 1, 페이스북으로 가입한적이 없다면 룸설정으로 0
	private int room_cnt = 0;
	public String room_no = "0";
	public String room_id = "";
	Button google_btn;
	private static final int GOOGLE_LOGIN = 1;
	
	//네이버
    /**
	 * client 정보를 넣어준다.
	 */
    private static String OAUTH_CLIENT_ID = "egk0cDijOiMnQgIKTjcA";
	private static String OAUTH_CLIENT_SECRET = "rp1k_TuVhu";
	private static String OAUTH_CLIENT_NAME = "웨딩박스";
	private static String OAUTH_CALLBACK_URL = "com.takebox.wedding";
	
	private OAuthLogin mOAuthLoginInstance;
	private Context mContext;
	private Button naver;
	
	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}
	
	String id=""; //로그인 이메일 아이디
	String files = "";
	String join_path = "";
	String email = ""; //sns 계정 아이디
	
	//카카오톡
	private LoginButton loginButton;
    private final SessionCallback mySessionCallback = new MySessionStatusCallback();
    private UserProfile userProfile;
	
    
    //구글로그인
  	private static final String TAG = "ExampleActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);

		//gcm 등록 
		registGCM();

		facebook_init(savedInstanceState);
		
		//카카오톡
		loginButton = (LoginButton) findViewById(R.id.activity_login_kakao_btn);
        loginButton.setLoginSessionCallback(mySessionCallback);
		
		Intent intent = getIntent();
  		room_no = intent.getStringExtra("room_no");
  		room_id = intent.getStringExtra("room_id");
  		
  		IntroActivity.activity.add(this);

		et_email = (EditText)findViewById(R.id.login_email_edit);
		et_email.setTypeface(CustomTextFont.NanumGothic);
		et_password = (EditText)findViewById(R.id.login_password_edit);
		et_password.setTypeface(CustomTextFont.NanumGothic);


		//로그인 버튼
		Button btn_login = (Button)findViewById(R.id.activity_login_ok_btn);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub



				//이메일 읿력 확인
				if(et_email.getText().toString().length() <=0 ){
					//입력 내용 없음 

					AlertDialog dialog = AlertDialogBuilder.pop_ok(LoginActivity.this, "이메일을 입력하세요.").create();    // 알림창 객체 생성
					dialog.show();   
					return;
				}


				//이메일 유효 확인
				if(!CommonUtil.isValidEmail(et_email.getText().toString())){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(LoginActivity.this, "이메일 형식에 맞지 않습니다.").create();    // 알림창 객체 생성
					dialog.show();    
					return;
				}


				//패스워드 입력 확인
				if(et_password.getText().toString().length() <=0 ){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(LoginActivity.this, "비밀번호를 입력하세요.").create();    // 알림창 객체 생성
					dialog.show();    
					return;
				}

				//서버 통신
				processParsing(mThread, login);

			}
		});



		//페이스북으로 로그인 하기
		btn_facebook = (ImageView)findViewById(R.id.login_facebook_btn);
		btn_facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sns_flag = "fb";
				inter_flag = true;
				facebook_login();
			}
		});
		
		//구글로그인
		mPlusClient = new PlusClient.Builder(this, this, this)
        .build();
		// 연결 실패가 해결되지 않은 경우 표시되는 진행률 표시줄입니다.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
		google_btn = (Button)findViewById(R.id.login_google_btn);
		google_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPlusClient.connect();
			}
		});
		
		//네이버로그인
		mContext = this;
		initNaverData();
		
		naver = (Button) findViewById(R.id.login_naver_btn);
		naver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
			}
		});
	}
	
	//네이버로그인 설정
	private void initNaverData() {
		mOAuthLoginInstance = OAuthLogin.getInstance();
		mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_CALLBACK_URL);
	}
	
	/**
	 * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다. 
	 * 네이버 로그인
	 */
	private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
		@Override
		public void run(boolean success) {
			if (success) {
				String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
				String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
				long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
				String tokenType = mOAuthLoginInstance.getTokenType(mContext);
				new RequestApiTask().execute();
			} else {
				String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
				String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
//				Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private class RequestApiTask extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected String doInBackground(Void... params) {
			String url = "https://apis.naver.com/nidlogin/nid/getUserProfile.xml";
			String at = mOAuthLoginInstance.getAccessToken(mContext);
			return mOAuthLoginInstance.requestApi(mContext, at, url);
		}
		protected void onPostExecute(String content) {
			content = content.replaceAll("<!", "");
			content = content.replaceAll("CDATA", "");
			content = content.replaceAll("]>", "]");
			DocumentBuilderFactory factory  =  DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				InputSource is = new InputSource();
			    is.setCharacterStream(new StringReader(content));
			    Document doc = builder.parse(is);
			    
			    nick_name = getNaverData(doc, "nickname");
			    sns_flag = "naver";
			    join_path = sns_flag;
				sns_id = getNaverData(doc, "email");
				_id = getNaverData(doc, "email");
				files = getNaverData(doc, "profile_image");
				
				processParsing(mThread, sns_join);
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getNaverData(Document doc, String index){
		String data     =  doc.getElementsByTagName(index).item(0).getTextContent();
		data = data.substring(2, data.indexOf("]"));
		
		return data;
		
	}
		
	
	//카카오톡
	protected void onResume() {
        super.onResume();
        // 세션을 초기화 한다
        if(com.kakao.Session.initializeSession(this, mySessionCallback)){
            // 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
        } else if (com.kakao.Session.getCurrentSession().isOpened()){
            // 2. 세션이 오픈된된 상태이면, 다음 activity로 이동한다.
          userProfile = UserProfile.loadFromCache();
          sns_flag = "kakao";
          inter_flag = true;
          processParsing(mThread, sns_join);
        }
            // 3. else 로그인 창이 보인다.
    }

    private class MySessionStatusCallback implements SessionCallback {
        /**
         * 세션이 오픈되었으면 가입페이지로 이동 한다.
         */
        @Override
        public void onSessionOpened() {
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈후 보일 페이지로 이동
        	LoginActivity.this.onSessionOpened();
        }

        /**
         * 세션이 삭제되었으니 로그인 화면이 보여야 한다.
         * @param exception  에러가 발생하여 close가 된 경우 해당 exception
         */
        @Override
        public void onSessionClosed(final KakaoException exception) {
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
        }

    }

    protected void onSessionOpened(){
        final Intent intent = new Intent(LoginActivity.this, com.takebox.wedding.KakaoTalkSignupActivity.class);
        startActivity(intent);
        finish();
    }



	private final Runnable sns_login = new Runnable() {
		@Override
		public void run() {
			sns_login();
		}
	};

	/**
	 * SNS회원 로그인 하기
	 */
	protected void sns_login() {
		// TODO Auto-generated method stub


		if(sns_flag.equals("fb")){
			try {
				Log.i("실행도", fb_obj+"0");
				_id = fb_obj.getString("email");
				Log.i("실행도", _id+"1");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{

		}

		Log.i("실행도", _id+"2");
		Map<String, String> data = new HashMap<String,String>();
		data.put("j_username", _id);
		data.put("j_password", "sns");


		if(GCM_CODE.isEmpty()){
			GCM_CODE = Info.GCM_REG_ID;
		}

		//GCM
		data.put("device_id", GCM_CODE);


		JSONObject obj = MemberModel.procMemberLogin(data);
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
				room_cnt = obj.getInt("room_cnt");
				handler.sendEmptyMessage(9);
			}else if(info.equals("fail")){
				//실패
				handler.sendEmptyMessage(10);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private final Runnable login = new Runnable() {
		@Override
		public void run() {
			login();
		}
	};

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				if(pd!=null) pd.cancel();
				//등록 성공
				member_login_save();


				if(room_id!=null){
					if(!room_id.equals("")){
						Log.i("room_id", room_id+"room");
						go_main();	//초대받고 들어온 이용자일때
					} else
						if(room_cnt==0)	//웨딩룸이 하나도 없을때
							go_start();
						else			//웨딩룸리스트로 이동
							go_roomlist();
				} else
					if(room_cnt==0)	//웨딩룸이 하나도 없을때
						go_start();
					else			//웨딩룸리스트로 이동
						go_roomlist();

				//go_main();

				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog = AlertDialogBuilder.pop_ok(LoginActivity.this, "회원정보를 다시 확인해 주세요.").create();
				dialog.show();   

				break;
				
			case 5:
				if(pd!=null) pd.cancel();
				//SNS회원가입 성공
				inter_flag = true;


				//SNS회원 로그인 
				processParsing(mThread, sns_login);

				break;
			case 6:
				if(pd!=null) pd.cancel();
//				Toast.makeText(IntroLoginActivity.this, "이미 가입된 사용자이므로 자동으로 로그인됩니다.", Toast.LENGTH_LONG).show();
				facebook_flag = 1;	//페이스북으로 가입한적이 있는사용자
				processParsing(mThread, sns_login);
				
				//SNS회원가입 실패
				break;
				
			case 9:
				if(pd!=null) pd.cancel();

				//로그인 성공
				member_login_save();

				if(room_id!=null){
					if(!room_id.equals("")){
						Log.i("room_id", room_id+"room");
						go_main();	//초대받고 들어온 이용자일때
					} else
						if(room_cnt==0)	//웨딩룸이 하나도 없을때
							go_start();
						else			//웨딩룸리스트로 이동
							go_roomlist();
				} else
					if(room_cnt==0)	//웨딩룸이 하나도 없을때
						go_start();
					else			//웨딩룸리스트로 이동
						go_roomlist();

				break;
			case 10:
				if(pd!=null) pd.cancel();
				
				AlertDialog dialog2 = AlertDialogBuilder.pop_ok(LoginActivity.this, "실패").create();
				dialog2.show();
				
				break;

			}
		}
	};


	protected void login() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();

		String email = et_email.getText().toString();
		String pwd = et_password.getText().toString();


		data.put("j_username",email);
		data.put("j_password", pwd);


		if(GCM_CODE.isEmpty()){
			GCM_CODE = Info.GCM_REG_ID;
		}

		//GCM
		data.put("device_id", GCM_CODE);



		//data.put("email", "");

		JSONObject obj = MemberModel.procMemberLogin(data);
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
				room_cnt = obj.getInt("room_cnt");
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
	/**
	 * 로그인 상태 저장
	 */
	protected void member_login_save() {
		// TODO Auto-generated method stub

		Map<String, String>data = new HashMap<String,String>();


		if(sns_flag.equals("fb")){
			data.put("ID", _id);
			data.put("PW", "sns");
			
			data.put("FB_ACCESS_TOKEN", fb_access_token);
			data.put("LOGIN_TYPE", "FB");

			
		}else if(sns_flag.equals("google")){
			data.put("ID", _id);
			data.put("PW", "sns");
			data.put("LOGIN_TYPE", "google");
		}else if(sns_flag.equals("naver")){
			data.put("ID", _id);
			data.put("PW", "sns");
			data.put("LOGIN_TYPE", "naver");
		}else if(sns_flag.equals("kakao")){
			data.put("ID", _id);
			data.put("PW", "sns");
			data.put("LOGIN_TYPE", "kakao");
		}else{
			data.put("ID", et_email.getText().toString());
			data.put("PW", et_password.getText().toString());
		}



		data.put("LOGIN_YN", "Y");
		data.put("JSESSIONID", Info.JSESSIONID);

		User.setUserInfo(data, LoginActivity.this);
	}


	//웨딩룸 리스트 이동
	protected void go_start() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(LoginActivity.this, StartActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
		overridePendingTransition(0, 0);
	} 
	
	/**
	 * roomlist 로 이동
	 */
	protected void go_roomlist() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(LoginActivity.this, RoomListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
		overridePendingTransition(0, 0);
	}


	protected void go_main() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("ROOM_ID", room_id);
		intent.putExtra("ROOM_SEQ", room_no);
		startActivity(intent);
		finish();
		overridePendingTransition(0, 0);
	}



	//구글 클라우드 메시지 사용자 코드 얻어 오기 
	private void registGCM() {

		if(!GCM_CODE.isEmpty()) return;

		try{

			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);

			final String regId = GCMRegistrar.getRegistrationId(this);
			System.out.println("***************** regId ***************** : " + regId);
			if("".equals(regId)){   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정

				GCMRegistrar.register(this, com.takebox.wedding.GCMIntentService.SEND_ID);

				String regId2 = GCMRegistrar.getRegistrationId(this);
				System.out.println("***************** regId2 ***************** : " + regId2);

				GCM_CODE = regId2;
				Log.i("tag", "1");
			}else{

				GCM_CODE = regId;
				Log.i("tag", "2");
			}
			
		}catch(Exception e){

		}

		System.out.println("GCM_CODE " + GCM_CODE);
	}






	private void facebook_init(Bundle savedInstanceState){
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
				session.openForRead(new Session.OpenRequest(this).setCallback(new StatusCallback() {

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




	private void facebook_login() {


		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session.isOpened()) {

			fb_access_token = session.getAccessToken();


			//페이스북 로그인 상태입니다.
			System.out.println("session.getAccessToken()" + fb_access_token);


			RequestBatch requestBatch = new RequestBatch();
			requestBatch.add(new Request(Session.getActiveSession(), 
					"me", null, null, new Request.Callback() {
				public void onCompleted(Response response) {

					String str = response.getRawResponse();

					try {
						System.out.println(str);
						fb_obj = new JSONObject(str);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 

					if(inter_flag){
						inter_flag = false;
						processParsing(mThread, sns_join);

					}



				}
			}));
			requestBatch.executeAsync();


		} else {

			//페이스북 로그인 상태가 아닙니다.

			//로그인 
			onClickLogin();

		}

	}
	
	private final Runnable sns_join = new Runnable() {
		@Override
		public void run() {
			sns_join();
		}
	};
	
	/**
	 * SNS 아이디로 회원가입 
	 */
	protected void sns_join() {
		// TODO Auto-generated method stub

		if(sns_flag.equals("fb")){


			try {

				Log.i("들어옴", "ㅇㅇ?");
				id = fb_obj.getString("id");
				email = fb_obj.getString("email");
				files = "https://graph.facebook.com/"+id+"/picture?width=200&height=200";
				join_path = sns_flag;
				nick_name = fb_obj.getString("name");


				_id = email; //로그인 아이디
				sns_id = id; // sns 아이디
				nick_name = fb_obj.getString("name");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}else if(sns_flag.equals("kakao")){
			nick_name = userProfile.getNickname();
			_id = userProfile.getId()+"@kakao.com";
			sns_id = userProfile.getId()+"@kakao.com";
			files = userProfile.getProfileImagePath();
			join_path = sns_flag;
		}

		Map<String, String> data = new HashMap<String,String>();
		data.put("id",_id);
		data.put("files", files);
		data.put("join_path", join_path);
		data.put("email", sns_id);
		data.put("nick_name", nick_name);

		JSONObject obj = MemberModel.procMemberSNSJoin(data);
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
				handler.sendEmptyMessage(5);
			}else if(info.equals("fail")){
				//실패
				handler.sendEmptyMessage(6);
			}
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	//페이스북 로그인 하기
	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
			.setCallback(new StatusCallback() {

				@Override
				public void call(Session session, SessionState state, Exception exception) {
					// TODO Auto-generated method stub
					if(session.isOpened()){
						facebook_login();
					}

				}
			})
			.setPermissions(Arrays.asList("email","public_profile","user_friends")));
		} else {
			Session.openActiveSession(this, true, 
					Arrays.asList("email","public_profile","user_friends"), 
					new StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					List<String> permission = session.getPermissions();
					// TODO Auto-generated method stub
					if(session.isOpened()){
						facebook_login();
					}
				}
			});
		}
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		
		if(resultCode != RESULT_OK){
			return;
		}
		
		switch(requestCode){

		case REQUEST_CODE_RESOLVE_ERR:
			
			mConnectionResult = null;
            mPlusClient.connect();
			
			break;
			
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}


	//로그아웃
	private void onClickLogout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                mPlusClient.connect();
            }
        }
        // 결과를 저장하고 사용자가 클릭할 때 연결 실패를 해결합니다.
        mConnectionResult = result;
	}

	@Override
	public void onConnected(Bundle arg0) {
		String name = mPlusClient.getCurrentPerson().getName().getFamilyName()+mPlusClient.getCurrentPerson().getName().getGivenName();
		String img_path = mPlusClient.getCurrentPerson().getImage().getUrl();
		img_path = img_path.substring(0, img_path.lastIndexOf("=")+1)+"200";
		
		_id = mPlusClient.getAccountName();
		files = img_path;
		join_path = "google";
		sns_flag = "google";
		email = mPlusClient.getAccountName();
		nick_name = name;
		
		processParsing(mThread, sns_join);
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG, "disconnected");
	}


}
