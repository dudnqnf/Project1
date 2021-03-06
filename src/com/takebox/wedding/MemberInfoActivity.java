package com.takebox.wedding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.Settings;
import com.google.analytics.tracking.android.EasyTracker;
import com.kakao.APIErrorResult;
import com.kakao.LogoutResponseCallback;
import com.kakao.SessionCallback;
import com.kakao.UserManagement;
import com.kakao.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.info.User;
import com.takebox.wedding.model.MemberModel;
import com.takebox.wedding.util.AndroidUtil;
import com.takebox.wedding.util.CommonUtil;
import com.takebox.wedding.util.HttpMultiPart;
import com.takebox.wedding.util.ImageReScale;
import com.takebox.wedding.util.ImageUpload;

/**
 * @author sujong
 * 메뉴 - 회원정보
 */
public class MemberInfoActivity extends HttpExceptionActivity {

	private Thread mThread = null;
	final int PICK_FROM_ALBUM = 1;
	final int CROP_FROM_ALBUM =2;
	
	FrameLayout btn_back;
	RelativeLayout btn_logout;
	ImageButton btn_complete;
	FrameLayout btn_profile;
	ImageView iv_img;
	EditText et_nickname;
	EditText et_pw;
	EditText et_pw_confirm;
	TextView id_txt;
	TextView infoAgreeTxt;
	TextView useAgreeTxt;
	
	String img;
	String snsImg;
	String nickname = "";
	String nickResultString = "";
	String pwResultString = "";
	String reviceName;
	String user_id;
	String user_type;
	private String file_name = "";
	String _img_url;
	
	AQuery aq;
	
	public static Bitmap profilePhoto = null;
	
	String profilePhotoPath;
	AndroidUtil andUtil = new AndroidUtil();
	InputMethodManager Imm;
	
	final String userProvisionURL = "http://app.takebox.co.kr/clause1.global";
	final String infoProvisionURL = "http://app.takebox.co.kr/clause2.global";
	
	private final SessionCallback mySessionCallback = new MySessionStatusCallback();
	
	private Uri uri;
	Uri profilePhotoUri;
	String path=Environment.getExternalStorageDirectory()+"";
	String img_file_name="temp";
	
	/**
	 * client 정보를 넣어준다.
	 */
	private static String OAUTH_CLIENT_ID = "egk0cDijOiMnQgIKTjcA";
	private static String OAUTH_CLIENT_SECRET = "rp1k_TuVhu";
	private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인?";
	private static String OAUTH_CALLBACK_URL = "http://static.nid.naver.com/oauth/naverOAuthExp.nhn";
	private OAuthLogin mOAuthLoginInstance;
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_info);
		
	//	Imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		Intent i = getIntent();
		
		//프로필사진
		img = getIntent().getStringExtra("u_picture");
		snsImg = getIntent().getStringExtra("u_sns_picture");
		nickname = getIntent().getStringExtra("u_nick");
		user_type = getIntent().getStringExtra("u_type");
		
		if(nickname.compareTo("null")==0){
			nickname = "";
		}
		
		
		user_id = getIntent().getStringExtra("u_id");
		
		Log.i("tag", "user_id : " + user_id);
		
		createUI();
		setProfileImage();
		setButtonEvent();
		setInfoData();
	}

	public void setInfoData(){
		et_nickname.setText(nickname);
		id_txt.setText(user_id);
		
	}
	
	public void setProfileImage(){
		
		if(img.length()==0&&snsImg.length()==0){
			iv_img.setImageResource(R.drawable.slide_menu_profile); //defaultImage;
		}else if(img.length()!=0){
			String img_name = img;
			String img_url = Info.MASTER_FILE_URL + "/image/"+ img_name;
			aq = new AQuery(MemberInfoActivity.this);
			aq.id(iv_img).image(img_url);
		} else if(snsImg.length()!=0){
			String img_name = snsImg;
			String img_url = img_name;
			aq = new AQuery(MemberInfoActivity.this);
			aq.id(iv_img).image(img_url, true, true);
		}
		
	}
	
	public void createUI(){
		//로그아웃 버튼
		btn_logout = (RelativeLayout)findViewById(R.id.re_lay03);
		iv_img = (ImageView)findViewById(R.id.member_info_img);
		AQuery aq= new AQuery(MemberInfoActivity.this);
		
		et_nickname = (EditText)findViewById(R.id.member_info_nickname_edit);
		
		et_pw = (EditText)findViewById(R.id.member_info_pw_edit);
		et_pw_confirm = (EditText)findViewById(R.id.member_info_pw_confirm_edit);
		if(user_type.compareTo("N")!=0){
			et_pw.setFocusable(false);
			et_pw_confirm.setFocusable(false);
			et_pw.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					baseToast("SNS가입회원은 비밀번호 변경이 불가합니다.");
				}
			});
			et_pw_confirm.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					baseToast("SNS가입회원은 비밀번호 변경이 불가합니다.");
				}
			});
		}
		
		Log.i("tag", "user_type : " + user_type);
		
		//닉네임 변경하기 버튼 
		btn_complete = (ImageButton)findViewById(R.id.member_info_nickname_btn);
		btn_profile = (FrameLayout)findViewById(R.id.member_info_img_btn);
		id_txt = (TextView)findViewById(R.id.text_id);
		btn_back = (FrameLayout)findViewById(R.id.btn_back);
		
		infoAgreeTxt = (TextView)findViewById(R.id.infoAgree);
		useAgreeTxt = (TextView)findViewById(R.id.useAgree);
		
	}
	
	public void setButtonEvent(){
		btn_complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean confirm = true;
				if(et_nickname.getEditableText().toString().length()==0){
					warningDialog("닉네임을 입력해주세요.");
					confirm = false;
				}else if(et_pw.getEditableText().toString().compareTo(et_pw_confirm.getEditableText().toString())!=0){
					//비밀번호 확인 일치
			    	if(et_pw.getEditableText().toString().length()==0 && et_pw_confirm.getEditableText().toString().length()==0){
			    		confirm = true;
			    	}else{
			    		warningDialog("비밀번호를 다시 확인해주세요.");
				    	confirm = false;
			    	}
				}else if(et_pw.getEditableText().toString().length() < 4){
			    	//비밀번호 4자리 이상
			    	if(et_pw.getEditableText().toString().length()==0 && et_pw_confirm.getEditableText().toString().length()==0){
			    		confirm = true;
			    	}else{
			    		warningDialog("비밀번호를 4자리 이상 입력해주세요.");
				    	confirm = false;
			    	}
			    }
				if(confirm)
					completeDialog();
				
			}
		});
		
		btn_profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doTakeAlbumAction();
			}
		});
		
		btn_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				logoutDialog();
			}
		});
		
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				go_back();
			}
		});
		
		
		infoAgreeTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MemberInfoActivity.this, MemberProvisionActivity.class);
				intent.putExtra("title", "개인정보 취급방침");
				intent.putExtra("url", infoProvisionURL);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
		useAgreeTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MemberInfoActivity.this, MemberProvisionActivity.class);
				intent.putExtra("title", "서비스 이용약관");
				intent.putExtra("url", userProvisionURL);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
		
	}

	public void logoutDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MemberInfoActivity.this);     // 여기서 this는 Activity의 this
		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("")        // 제목 설정
		.setMessage("로그아웃 하시겠습니까?")        // 메세지 설정
		.setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
			// 확인 버튼 클릭시 설정
			public void onClick(DialogInterface dialog, int whichButton){
				//로그아웃
				com.kakao.Session.initializeSession(MemberInfoActivity.this, mySessionCallback);
				if(com.kakao.Session.getCurrentSession().isOpened() || com.kakao.Session.getCurrentSession().isClosed())
					kakao_logout();
				processParsing(mThread, member_logout);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private class MySessionStatusCallback implements SessionCallback {
        @Override
        public void onSessionOpened() {
        }
        @Override
        public void onSessionClosed(final KakaoException exception) {
        }

    }
	
	public void completeDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MemberInfoActivity.this);     // 여기서 this는 Activity의 this
		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("")        // 제목 설정
		.setMessage("회원정보를 변경하시겠습니까??")        // 메세지 설정
		.setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				et_nickname.setText(nickname);
				dialog.dismiss();
			}
		})
		.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
			// 확인 버튼 클릭시 설정
			public void onClick(DialogInterface dialog, int whichButton){
				reviceName = et_nickname.getEditableText().toString();
				sendNickname(reviceName);
				Thread thread = new Thread();
				processParsing(thread, runOfreviceNickname);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void warningDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(MemberInfoActivity.this);     // 여기서 this는 Activity의 this
		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("")        // 제목 설정
		.setMessage(msg)        // 메세지 설정
		.setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
		.setNegativeButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				et_nickname.setText(nickname);
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void doTakeAlbumAction(){
		System.out.println("doTakeAlbumAction");
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, PICK_FROM_ALBUM);
		overridePendingTransition(0,0);
	}
	
	private final Runnable runOfreviceNickname = new Runnable() {
		@Override
		public void run() {
			sendNickname(reviceName);
			MainActivity.firstExcutePlug = false;
		}
	};
	
	private final Runnable runOfImageUpload = new Runnable() {
		@Override
		public void run() {
			uploadImage();
			MainActivity.firstExcutePlug = false;
		}
	};
	
	protected void uploadImage() {
		
		JSONObject obj = ImageUpload.imgUpload(Info.MASTER_URL+"/userProfileUpload.take", profilePhoto, profilePhotoPath);
		if(!isHttpWorthCheck(obj)){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}
		String info;
		try {
			info = obj.getString("info");
			if(info.equals("ok")){
				
				img = obj.getString("fileName");
				handler.sendEmptyMessage(3);
				
//				Intent intent = new Intent(MemberInfoActivity.this, MemberInfoActivity.class);
//				intent.putExtra("u_picture", img);
//				intent.putExtra("u_nick", nickname);
//				intent.putExtra("u_type", user_type);
//				startActivity(intent);
//				overridePendingTransition(0, 0);
//				finish();

			}else{

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendNickname(String _reviceName){
		
		Map<String, String> data = new HashMap<String,String>();
		data.put("nickname", _reviceName);
		HttpMultiPart hmp = new HttpMultiPart();
		try {
			
			//닉네임 변경
			System.out.println("req : " +  data);
			String nickRes = hmp.transfer(Info.MASTER_URL+"/updateNickName.take", data);
			System.out.println("res : " + nickRes.trim());
			JSONObject nickObj = new JSONObject(nickRes.trim());
			if(!isHttpWorthCheck(nickObj)){
				if(pd.isShowing())
					pd.dismiss();
				return;
			}
			nickResultString = (String) nickObj.get("info");

			data.clear();
			
			if(et_pw.getEditableText().toString().length() != 0){
				data.put("password", et_pw.getEditableText().toString());
				//비밀번호 변경
				System.out.println("req : " +  data);
				String pwRes = hmp.transfer(Info.MASTER_URL+"/updatePassword.take", data);
				System.out.println("res : " + pwRes.trim());
				JSONObject pwObj = new JSONObject(pwRes.trim());
				pwResultString = (String) nickObj.get("info");
			}
			handler.sendEmptyMessage(2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		
		if(resultCode != RESULT_OK){
			return;
		}

		switch(requestCode){
		
			case PICK_FROM_ALBUM:
				
			profilePhotoUri = data.getData();
			profilePhotoPath = andUtil.getRealPathFromURI(MemberInfoActivity.this, profilePhotoUri);
			path = profilePhotoPath.substring(0, profilePhotoPath.lastIndexOf("/"));
			img_file_name = System.currentTimeMillis()+".jpg";
			Log.i("path", path);
			
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(data.getData(), "image/*");
			intent.putExtra("outputX", 200); // crop한 이미지의 x축 크기
			intent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
			intent.putExtra("aspectX", 1); // crop 박스의 x축 비율 
			intent.putExtra("aspectY", 1); // crop 박스의 y축 비율
			intent.putExtra("scale", true);
			intent.putExtra("output", profilePhotoUri);
			
			File f = new File(path, "/"+img_file_name);
			try {
				f.createNewFile();
			} catch (IOException ex) {
				Log.e("io", ex.getMessage());  
			}
			
			uri = Uri.fromFile(f);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			
			startActivityForResult(intent, CROP_FROM_ALBUM);
			
			break;

		case CROP_FROM_ALBUM:
			try {
				System.out.println("pick_from_album data !!!");
				
				String filePath = path + "/" + img_file_name;
				Log.i("filePath", filePath);
				profilePhotoUri = Uri.parse(filePath);
				
				profilePhotoPath = andUtil.getRealPathFromURI(MemberInfoActivity.this, profilePhotoUri);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;
				
				if(profilePhoto != null){
					profilePhoto.recycle();
					profilePhoto = null;
				}
				
				//이미지 사이즈 화면 사이즈에 맞게 리스케일
				ImageReScale imgReScale = new ImageReScale();
				profilePhoto = imgReScale.loadBackgroundBitmap(getApplicationContext(), profilePhotoPath);	

				setImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void setImage()
	{
		//미디어 스캐닝
		int version = android.os.Build.VERSION.SDK_INT;
		  
		  if (version > 17) {   
		   Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);  
		      Uri contentUri = Uri.parse("file://" + Environment.getExternalStorageDirectory());
		      mediaScanIntent.setData(contentUri);
		      sendBroadcast(mediaScanIntent);
		  } else {
			  sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		  }		
		try {
			//mPhotoButton.setVisibility(View.GONE);
			//mPhotoImageView.setVisibility(View.VISIBLE);

			Bitmap tbm = null;
			if(profilePhoto != null) 
			{
				// 이미지를 상황에 맞게 회전시킨다
				profilePhoto = andUtil.bitmapRotate(profilePhotoPath, profilePhoto);

				//imageview에 큰 사이트가 들어가면 out of memory가 걸려서 축소시킨다.
				tbm = Bitmap.createScaledBitmap(profilePhoto, 200, 200, true);
				//mPhotoImageView.setImageBitmap(tbm);
			} else {

				File file = new File(profilePhotoPath);

				if(file.exists()) {
					//이미지 사이즈 화면 사이즈에 맞게 리스케일
					ImageReScale imgReScale = new ImageReScale();
					profilePhoto = imgReScale.loadBackgroundBitmap(MemberInfoActivity.this, profilePhotoPath);	
					profilePhoto = andUtil.bitmapRotate(profilePhotoPath, profilePhoto);

					tbm = Bitmap.createScaledBitmap(profilePhoto, 200, 200, true);
					//mPhotoImageView.setImageBitmap(tbm);
				}
			}

			file_name = CommonUtil.getFileNameWithoutExtension(profilePhotoPath);

			//			int width, height;
			//			
			//			width = selPhoto.getWidth();
			//			height = selPhoto.getHeight();

			System.out.println("width = " + profilePhoto.getWidth());
			System.out.println("height = " + profilePhoto.getHeight());

			//			iv_img.setImageBitmap(tbm);
			//			iv_img.invalidate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error" + e);
		}

		//앨범 내용 작성
		Thread thread = new Thread();
		processParsing(thread, runOfImageUpload);
	}
	private final Runnable member_logout = new Runnable() {
		@Override
		public void run() {
			member_logout();
		}
	};

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {
			case 1:
				if(pd!=null) pd.cancel();
				//로그아웃 됨.
				
				//정보 삭제
				Map<String,String> data = new HashMap<String,String>();
				
				data.put("ID", "");
				data.put("PW", "");
				data.put("LOGIN_YN", "");
				data.put("JSESSIONID", "");
				
				User.setUserInfo(data, MemberInfoActivity.this);
				
				Info.JSESSIONID = "";
				Info.GCM_REG_ID = "";
				
				Intent intent = new Intent(MemberInfoActivity.this, IntroLoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				for (int i = 0; i < IntroActivity.activity.size(); i++) {
					IntroActivity.activity.get(i).finish();
				}
				finish();
				overridePendingTransition(0, 0);

				break;
				
			case 2:
				
				if((nickResultString.compareTo("ok")==0 && pwResultString.compareTo("ok")==0) ||
				   (nickResultString.compareTo("ok")==0 && pwResultString.compareTo("")==0)){
					if(et_pw.getEditableText().toString().length()==0 && et_pw_confirm.getEditableText().toString().length()==0)
					nickname = reviceName;
					String RESULT_OK_MSG = "정보가 변경되었습니다.";
					Toast.makeText(MemberInfoActivity.this, RESULT_OK_MSG, Toast.LENGTH_SHORT).show();
					go_back();
				}else{
					String RESULT_FAIL_MSG = "다시 시도해주세요.";
					et_nickname.setText(nickname); //닉네임 변경 실패시 최근 변경된 이름으로 TextEdit 유지.
					Toast.makeText(MemberInfoActivity.this, RESULT_FAIL_MSG, Toast.LENGTH_SHORT).show();
				}
				pd.cancel();
			break;
			
			case 3:
			
				setProfileImage();
				pd.cancel();
				Toast.makeText(MemberInfoActivity.this, "이미지가 변경되었습니다.", Toast.LENGTH_SHORT).show();
				
			break;
			
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		//analytics 분석도구 
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}

	//로그아웃
	protected void member_logout() {
		// TODO Auto-generated method stub
		String res = MemberModel.procMemberLogout(null);
		if(res==null){
			if(pd.isShowing())
				pd.dismiss();
			return;
		}
		//네이버 로그아웃
		mOAuthLoginInstance = OAuthLogin.getInstance();
		mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_CALLBACK_URL);
		mOAuthLoginInstance.logout(MemberInfoActivity.this);
		
		//페이스북 로그아웃
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();	
		if(session != null){
			session.closeAndClearTokenInformation();
		}
		
		handler.sendEmptyMessage(1);
		
		
		
	}
	
	public void kakao_logout(){
		//카카오톡 로그아웃
		UserManagement.requestLogout(new LogoutResponseCallback() {
	        @Override
	        protected void onSuccess(final long userId) {
	        	//성공
	        	com.kakao.Session.getCurrentSession().close(new SessionCallback() {
					@Override
					public void onSessionOpened() {
					}
					@Override
					public void onSessionClosed(KakaoException exception) {
					}
				});
	        }
	        
	        @Override
	        protected void onFailure(final APIErrorResult apiErrorResult) {
//	        	Toast.makeText(MemberInfoActivity.this, "카카오톡 로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show();
	        	return;
	        }
	    });
	}

	@Override
	public void onStop() {
		super.onStop();
		//analytics 분석도구 
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}
	
	@Override
	public void onBackPressed() {
		go_back();
	}
	
	public void go_back(){
		Intent intent = new Intent(MemberInfoActivity.this, MainSetupActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("u_picture", img);
		intent.putExtra("u_nick", nickname);
		intent.putExtra("u_type", user_type);
		setResult(RESULT_OK, intent);
		finish();
		overridePendingTransition(0, 0);
	}

}


