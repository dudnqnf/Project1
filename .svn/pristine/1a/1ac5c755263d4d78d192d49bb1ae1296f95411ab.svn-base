package com.takebox.wedding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.pulltorefresh.PullToRefreshBase;
import com.takebox.wedding.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.takebox.wedding.pulltorefresh.PullToRefreshListView;
import com.takebox.wedding.slidingmenu.SlidingMenu;
import com.takebox.wedding.slidingmenu.SlidingMenu.OnCloseListener;
import com.takebox.wedding.slidingmenu.SlidingMenu.OnOpenListener;
import com.takebox.wedding.dialog.AlbumCategoryDialog;
import com.takebox.wedding.dialog.AlbumWriteDialog;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.dialog.PhotoAlbumDialog;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.info.User;
import com.takebox.wedding.model.AlbumModel;
import com.takebox.wedding.model.MemberModel;
import com.takebox.wedding.model.WeddingModel;
import com.takebox.wedding.util.AndroidUtil;
import com.takebox.wedding.util.BackPressCloseHandler;
import com.takebox.wedding.util.CommonUtil;
import com.takebox.wedding.util.CustomTextFont;
import com.takebox.wedding.util.ImageReScale;
import com.takebox.wedding.R;

public class MainActivity extends HttpExceptionActivity implements
		OnClickListener {

	private PullToRefreshListView pullToRefreshView;
	private ArrayList<Item> listItem;
	private ItemAdapter adapter;

	private ListView lv_list;
	private View header;
	public static SlidingMenu menu;

	private Thread mThread = null;

	private JSONObject room_info;
	private JSONArray contents;
	private JSONArray cate_list;
	private JSONArray cate_sgt_list; // 카테고리 추천리스트
	private JSONObject user_info;

	private String adminAuth;

	private String room_id;
	private String room_seq;
	
	private long backKeyPressedTime = 0;
	private Toast toast;
	private FrameLayout shadowlayout;

	// 뒤로가기 버튼 두번 종료
	private BackPressCloseHandler backPressCloseHandler;

	// 카메라 액티비티 리턴
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int PICK_FROM_MULTI_ALBUM = 2;
	private static final int PICK_FROM_CAPTURE_MOVIE = 3;
	private static final int PICK_FROM_SELECT_VIDEO = 4;
	private static final int LIMIT_VIDEO_SIZE = 104857600; //100MB
	private static final int FACEBOOK_FRIEND_LIST = 5;
	
	private AndroidUtil andUtil = new AndroidUtil();
	public static Bitmap selPhoto = null;
	private String selPhotoPath;
	private String file_name = "";

	private ImageView iv_img;

	private String cata_seq; // 사진 등록 카테고리
	private String content; // 사진 내용

	private ByteArrayOutputStream[] bos = null;
	private Bitmap[] bm = null;
	private String[] all_path = null;

	int scroll_value = 0;
	private Boolean flag = true;

	String menu_profile_nick;
	String menu_profile_picture;
	String menu_profile_sns_picture;
	String menu_profile_regdate;
	String userId;

	
	
	private boolean singlePicturePlug = false;
	public static boolean firstExcutePlug = true; // 메인페이지 새로고침 여부

	public static String cate_seq = "";
	public static String cate_name = "전체";
	public static String photourl;

	private final String ADMIN_AUTH_MESSAGE = "초대는 주인공만 할 수 있습니다.";
	private Boolean fb_login_click = false;
	private String fb_access_token;

	public static Uri imageUri;
	
	public LinearLayout ll_tab_box;
	public Button btn_upload;
	public Button btn_invite;
	public Button btn_shopping;
	
	private String thumbFilename = "";
	private String videoPath = "";
	private File vedioFile = null;
	private Uri mVideoCaptureUri = null;
	
	private UiLifecycleHelper uiHelper;

	//하단 탭바 애니메이션 부분
	boolean tabAnimationPlug = false;
	boolean firstDragFlag = true;
	boolean dragFlag = false;   //현재 터치가 드래그 인지 확인
	float startYPosition = 0;       //터치이벤트의 시작점의 Y(세로)위치
	float endYPosition = 0;
	public static String editedComment = "";
	public static String editedContent = "";
	public static int editedListPos = -1;
	
	public ImageView defualt_img;		//메인 디폴트 이미지
	public static boolean isDeletedRoom = false;
	
	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	public void onResume() {
		super.onResume();
		//현재방이 삭제되었을시
		if(isDeletedRoom){		
			isDeletedRoom=false;
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("")        // 제목 설정
			.setMessage("방이 삭제되었습니다.")        // 메세지 설정
			.setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
			.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
				// 확인 버튼 클릭시 설정
				public void onClick(DialogInterface dialog, int whichButton){
					Intent intent = new Intent(MainActivity.this, RoomListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
					finish();
					overridePendingTransition(0, 0);
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		if (!firstExcutePlug) {
			processParsing(mThread, getWeddigRoomData); // firstExcutePlug플레그
														// 수정은 getWeddigRoomData
														// 쓰레드 부분
			firstExcutePlug = true;
		}
		
		if(editedListPos>=0){		//리스트가 변경되었을때
			Item newItem = new Item();
			newItem.con_seq = listItem.get(editedListPos).con_seq;
			newItem.in_u_seq = listItem.get(editedListPos).in_u_seq;
			newItem.in_u_id = listItem.get(editedListPos).in_u_id;
			newItem.wed_seq = listItem.get(editedListPos).wed_seq;
			
			if(!editedContent.equals(""))
				newItem.content = editedContent;
			else
				newItem.content = listItem.get(editedListPos).content;
			
			newItem.file_name = listItem.get(editedListPos).file_name;
			newItem.like_count = listItem.get(editedListPos).like_count;
			newItem.cancle_yn = listItem.get(editedListPos).cancle_yn;
			newItem.u_seq = listItem.get(editedListPos).u_seq;
			
			if(editedComment.equals("add"))
				newItem.cmt_cnt = (Integer.parseInt(listItem.get(editedListPos).cmt_cnt) + 1)+"";
			else
				newItem.cmt_cnt = listItem.get(editedListPos).cmt_cnt;
			
			newItem.cate_seq = listItem.get(editedListPos).cate_seq;
			newItem.in_u_nick = listItem.get(editedListPos).in_u_nick;
			newItem.in_u_picture = listItem.get(editedListPos).in_u_picture;
			newItem.in_u_sns_picture = listItem.get(editedListPos).in_u_sns_picture;
			newItem.reg_date = listItem.get(editedListPos).reg_date;
			newItem.video_yn = listItem.get(editedListPos).video_yn;
			newItem.img = Info.MASTER_FILE_URL + "/image/" + newItem.file_name;

			listItem.set(editedListPos, newItem);
			adapter.notifyDataSetChanged();
			
			editedContent = "";			//초기화
			editedComment = "";			//초기화
			editedListPos = -1;			//초기화
		}
		
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		if(hasFocus == true){
			onResume();
		} else {
			onPause();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		uiHelper = new UiLifecycleHelper(this, null);
	    uiHelper.onCreate(savedInstanceState);

		facebook_init(savedInstanceState);
		
		Log.i("SESSIONID", User.getUserInfo("ID", MainActivity.this));
		Log.i("SESSIONPW", User.getUserInfo("PW", MainActivity.this));

		if (getIntent().getStringExtra("ROOM_ID") != null) {

			room_id = getIntent().getStringExtra("ROOM_ID");
			room_seq = getIntent().getStringExtra("ROOM_SEQ");

		}

		// 새로고침 뷰
		pullToRefreshView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
		pullToRefreshView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// Do work to refresh the list here.
						// 당겨서 새로고침
						processParsing(mThread, getWeddigRoomData);

					}
				});
	
		lv_list = pullToRefreshView.getRefreshableView();

		// 리스트 위에 영역(프로필 )
		header = getLayoutInflater().inflate(R.layout.main_profile_header,
				null, false);
		lv_list.addHeaderView(header);
		
		ll_tab_box = (LinearLayout) findViewById(R.id.main_tab);

		// 메뉴 버튼
		FrameLayout btn_menu = (FrameLayout) findViewById(R.id.main_menu_btn);
		btn_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (menu.isShown()) {
					menu.showMenu();
				} else {
					menu.showContent();
				}
			}
		});

		// 홈 버튼
		FrameLayout btn_home = (FrameLayout) findViewById(R.id.btn_back);
		btn_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_upload.setBackgroundResource(R.drawable.btn_photo);
				btn_invite.setBackgroundResource(R.drawable.btn_invite);
				btn_shopping.setBackgroundResource(R.drawable.btn_shopping);
			}
		});

		// 웨딩룸 데이터 가져오기
		processParsing(mThread, getWeddigRoomData);

		// 프로필설정하기 버튼
		FrameLayout btn_profile_config = (FrameLayout) findViewById(R.id.header_profile_config_btn);
		btn_profile_config.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				go_profile_view();
			}
		});

		// 앨범 카테고리 선택
		RelativeLayout btn_album_category = (RelativeLayout) findViewById(R.id.album_category);
		TextView btn_album_category_text = (TextView)findViewById(R.id.album_category_text);
		btn_album_category_text.setTypeface(CustomTextFont.hunsomsatangR);
		btn_album_category.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 다이얼로그
				AlbumCategoryDialog pop_category = new AlbumCategoryDialog(
						MainActivity.this, MainActivity.this, cate_list,
						room_seq, cate_sgt_list, room_id, adminAuth);
				pop_category.show();
			}
		});

		/*
		 * 상단 탭
		 */
		// 하객초대
		btn_invite = (Button) findViewById(R.id.main_invite_btn);
		btn_invite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 초대하기

				if (adminAuth.compareTo("admin") == 0) {
					Intent intent = new Intent(MainActivity.this,
							InviteFriendActivity.class);
					try {
						intent.putExtra("profile_img", room_info.getString("profile_img"));
						intent.putExtra("wed_date", room_info.getString("wed_date"));
						intent.putExtra("wed_time", room_info.getString("wed_time"));
						intent.putExtra("place", room_info.getString("place"));
						intent.putExtra("room_id", room_info.getString("room_id"));
						intent.putExtra("room_no", room_seq);
						intent.putExtra("description", room_info.getString("description"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startActivity(intent);
					overridePendingTransition(0, 0);

				} else {
					baseToast(ADMIN_AUTH_MESSAGE);
				}
			}
		});

		// 업로드
		btn_upload = (Button) findViewById(R.id.main_upload_btn);
		btn_upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					// 사진 앨범
					// 사진앨범 다이얼로그
				final PhotoAlbumDialog pop_photo = new PhotoAlbumDialog(
						MainActivity.this, MainActivity.this, "N");
				pop_photo.url = "DCIM/AT"
						+ String.valueOf(System.currentTimeMillis())
						+ ".jpg";
				photourl = pop_photo.url;
				String img_url = "/mnt/sdcard/" + pop_photo.url;
				selPhotoPath = img_url;
				pop_photo.show();
			}
		});
		
		defualt_img = (ImageView)findViewById(R.id.main_default_img);
		defualt_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					// 사진 앨범
					// 사진앨범 다이얼로그
				final PhotoAlbumDialog pop_photo = new PhotoAlbumDialog(
						MainActivity.this, MainActivity.this, "N");
				pop_photo.url = "DCIM/AT"
						+ String.valueOf(System.currentTimeMillis())
						+ ".jpg";
				photourl = pop_photo.url;
				String img_url = "/mnt/sdcard/" + pop_photo.url;
				selPhotoPath = img_url;
				pop_photo.show();
			}
		});
		
		shadowlayout = (FrameLayout) findViewById(R.id.ShadowLayout);

		// 쇼핑
		btn_shopping = (Button) findViewById(R.id.main_shopping_btn);
		btn_shopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});

		// 슬라이드 메뉴
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffset(180);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setOnOpenListener(new OnOpenListener() {

			@Override
			public void onOpen() {
				shadowlayout.setVisibility(View.VISIBLE);
			}
		});
		menu.setOnCloseListener(new OnCloseListener() {

			@Override
			public void onClose() {
				shadowlayout.setVisibility(View.INVISIBLE);
			}
		});
		menu.setMenu(R.layout.menu);
		goToInitialAppInfo(InitialAppInfoActivity.kindOfActivity[0]);
		/*
		 * 뒤로가기 버튼 두번 종료
		 */
		backPressCloseHandler = new BackPressCloseHandler(this);
	}

	public void setTabbarScrollAnimation(int count){
	
		final AnimationSet set01 = new AnimationSet(true);
		final TranslateAnimation animation01 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF,	0.5f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		final AnimationSet set02 = new AnimationSet(true);
		final TranslateAnimation animation02 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF,	0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		
		if(count!=0){
			lv_list.setOnTouchListener(new View.OnTouchListener() {
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			        switch (event.getAction()) {
			        case MotionEvent.ACTION_MOVE:       //터치를 한 후 움직이고 있으면
			            dragFlag = true;
			            if(firstDragFlag) {     //터치후 계속 드래그 하고 있다면 ACTION_MOVE가 계속 일어날 것임으로 무브를 시작한 첫번째 터치만 값을 저장함
			                startYPosition = event.getY(); //첫번째 터치의 Y(높이)를 저장
			                firstDragFlag= false;   //두번째 MOVE가 실행되지 못하도록 플래그 변경
			            }
	
			            if(event.getY() < startYPosition){
		    				if(!tabAnimationPlug){
				    			ll_tab_box.setVisibility(View.GONE);
				    			set02.setFillAfter(false);
				    			animation02.setDuration(300);
				    			set02.addAnimation(animation02);
				    			ll_tab_box.startAnimation(set02);
				    			tabAnimationPlug = true;
			    			}
		    			}
		    			if(event.getY() > startYPosition){
		    				if(tabAnimationPlug){
			    				ll_tab_box.setVisibility(View.VISIBLE);
				    			set01.setFillAfter(false);
				    			animation01.setDuration(300);
				    			set01.addAnimation(animation01);
				    			ll_tab_box.startAnimation(set01);
				    			tabAnimationPlug = false;
			    			}
		    			}
			            
			            break;
			 
			        case MotionEvent.ACTION_UP : 
			            endYPosition = event.getY();
			            firstDragFlag= true;
			 
			            if(dragFlag) {  //드래그를 하다가 터치를 실행
			                // 시작Y가 끝 Y보다 크다면 터치가 아래서 위로 이루어졌다는 것이고, 스크롤은 아래로내려갔다는 뜻이다.
			                // (startYPosition - endYPosition) > 10 은 터치로 이동한 거리가 10픽셀 이상은 이동해야 스크롤 이동으로 감지하겠다는 뜻임으로 필요하지 않으면 제거해도 된다.
			                if((startYPosition > endYPosition) && (startYPosition - endYPosition) > 10) {
			                    //TODO 스크롤 다운 시 작업
			                } 
			                //시작 Y가 끝 보다 작다면 터치가 위에서 아래로 이러우졌다는 것이고, 스크롤이 올라갔다는 뜻이다.
			                else if((startYPosition < endYPosition) && (endYPosition - startYPosition) > 10) {
			                    //TODO 스크롤 업 시 작업
			                }
			            }
			 
			            startYPosition = 0.0f;
			            endYPosition = 0.0f;
			            break;
			        }
			        return false;
			    }
			});
		}else{
			ll_tab_box.setVisibility(View.VISIBLE);
			lv_list.setOnTouchListener(null);
		}
		
	}
	
	public void finishBeforeActivity() {
		if (IntroActivity.activity != null) {
			if (IntroActivity.activity.size() == 0)
				return;
			ArrayList<Activity> actList = IntroActivity.activity;
			if (IntroActivity.activity.contains(MainActivity.this)){
				actList.remove(MainActivity.this);
			}
			for (int i = 0; i < IntroActivity.activity.size(); i++) {
				actList.get(i).finish();
			}
		}
	}

	/*
	 * 
	 * 슬라이드 메뉴
	 */
	public void show_menu() {

		String u_picture = "null";
		String u_id = "null";
		String u_seq = "null";
		String u_nick = "null";
		String u_sns_picture = "null";
		try {
			u_picture = user_info.getString("u_picture").toString();
			u_id = user_info.getString("u_id").toString();
			u_seq = user_info.getString("u_seq").toString();
			u_nick = user_info.getString("u_nick").toString();
			u_sns_picture = user_info.getString("u_sns_picture").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Log.i("메뉴 정보 불러오기", u_id);

		// 슬라이드 메뉴 프로필 이미지
		if (!u_picture.equalsIgnoreCase("null")
				&& !u_picture.equalsIgnoreCase("")) {
			String img_name = u_picture;
			// Log.i("u_picture!", img_name);
			ImageView profile_img = (ImageView) menu
					.findViewById(R.id.menu_profile_img);
			String img_url = Info.MASTER_FILE_URL + "/image/" + img_name;
			AQuery aq = new AQuery(MainActivity.this);
			aq.id(profile_img).image(img_url, true, true);
		} else if (!u_sns_picture.equalsIgnoreCase("null")
				&& !u_sns_picture.equalsIgnoreCase("")) {
			String img_name = u_sns_picture;
			// Log.i("u_sns_picture!", img_name);
			ImageView profile_img = (ImageView) menu
					.findViewById(R.id.menu_profile_img);
			String img_url = img_name;
			AQuery aq = new AQuery(MainActivity.this);
			aq.id(profile_img).image(img_url, true, true);
		}

		// 슬라이드 메뉴 프로필 이름
		TextView tv = (TextView) menu.findViewById(R.id.menu_profile_text2);
		tv.setTypeface(CustomTextFont.NanumGothic);
		if(!u_nick.equals("") && !u_nick.equals("null")){
			tv.setText(u_nick);
		} else {
			tv.setText(u_id);
		}
		
		
		FrameLayout btn_menu_invite_list = (FrameLayout) menu
				.findViewById(R.id.menu_invite_list_btn);
		FrameLayout btn_menu_profile = (FrameLayout) menu
				.findViewById(R.id.menu_profile_btn);
		FrameLayout btn_menu_facebook_share = (FrameLayout) menu
				.findViewById(R.id.menu_facebook_share_btn);
		FrameLayout btn_menu_room_list = (FrameLayout) menu
				.findViewById(R.id.menu_room_list_btn);
		FrameLayout btn_menu_setup = (FrameLayout) menu
				.findViewById(R.id.menu_setup_btn);

		if (adminAuth.compareTo("admin") == 0) {
			btn_menu_facebook_share.setVisibility(View.VISIBLE);
			btn_menu_profile.setVisibility(View.VISIBLE);
		} else {
			btn_menu_facebook_share.setVisibility(View.GONE);
			btn_menu_profile.setVisibility(View.GONE);
		}

		btn_menu_invite_list.setOnClickListener(this);
		btn_menu_profile.setOnClickListener(this);
		// 슬라이드메뉴 페이스북공유하기 버튼

		// btn_menu_facebook_share.setOnClickListener(this);
		btn_menu_facebook_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				facebookLogin();
			}
		});

		btn_menu_room_list.setOnClickListener(this);
		btn_menu_setup.setOnClickListener(this);
		FrameLayout btn_menu_qna = (FrameLayout)menu.findViewById(R.id.menu_qna_btn);
		btn_menu_qna.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, QnAActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
	}

	private void show_category() {
		TextView tv = (TextView) findViewById(R.id.album_category_text);
		tv.setText(cate_name);
	}


	private void facebookLogin(){
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			
			sendRequestDialog();
			
//			RequestBatch requestBatch = new RequestBatch();
//			requestBatch
//			.add(new Request(Session.getActiveSession(), 
//					"/me/friends", null, null, new Request.Callback() {
//				public void onCompleted(Response response) {
//					Intent intent = new Intent(MainActivity.this, FacebookFriendList.class);
					
//					JSONObject obj = response.getGraphObject().getInnerJSONObject();
//					Log.i("RESPONSE", response+"!");
//					
//					String []name = null;
//					String []id = null;
//					try {
//						int leng;
//						leng = obj.getJSONArray("data").length();
//						name = new String[leng];
//						id = new String[leng];
//						for(int i = 0; i<leng; i++){
//							name[i] = obj.getJSONArray("data").getJSONObject(i).getString("name");
//							id[i] = obj.getJSONArray("data").getJSONObject(i).getString("id");
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					
//					intent.putExtra("name", name);
//					intent.putExtra("id", id);
//					startActivityForResult(intent, FACEBOOK_FRIEND_LIST);
					
					
//					sendRequestDialog();
//				}
//			}));
			
//			requestBatch.executeAsync();
			
		} else {

			//로그인 
			onClickLogin();

		}
	}
	
	private void sendRequestDialog() {
		String female_name="";
		String male_name="";
		String room_id="";
		String link="";
		String description="";
		String img="";
		try {
			female_name = room_info.getString("female_name");
			male_name = room_info.getString("male_name");
			room_id = room_info.getString("room_id");
			description = room_info.getString("description");
			link = Info.PC_MARKET_URL;
			img = room_info.getString("profile_img");
			if(img.equals("") || img.equals("null")){
				img = Info.DEFAULT_IMG;
			} else {
				img = Info.MASTER_FILE_URL + "/image/" + room_info.getString("profile_img");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
			.setName(female_name + " ♡ " +male_name)
			.setCaption(room_id)
			.setDescription(description)
			.setLink(link)
			.setPicture(img)
			.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
			
		} else {
			Bundle params = new Bundle();
		    params.putString("name", male_name + " ♡ " + female_name);
			params.putString("caption", "웨딩ID : "+ room_id);
			params.putString("description", description);
			params.putString("link", link);
			params.putString("picture", img);

			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
				MainActivity.this, Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {
					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(MainActivity.this,
										"게시되었습니다.",
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"취소되었습니다.", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(
									MainActivity.this.getApplicationContext(),
									"취소되었습니다.", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(
									MainActivity.this.getApplicationContext(),
									"게시하는 도중 오류가 발생하였습니다.", Toast.LENGTH_SHORT)
									.show();
						}
					}
	
				}).build();
			feedDialog.show();
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
						facebookLogin();
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
					// TODO Auto-generated method stub
					if(session.isOpened()){
						facebookLogin();
					}
				}
			});
		}
	}
	

	private void facebook_init(Bundle savedInstanceState) {
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null,
						new StatusCallback() {

							@Override
							public void call(Session session,
									SessionState state, Exception exception) {
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


	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			shadowlayout.setVisibility(View.INVISIBLE);
			menu.toggle();
		}else {
			if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
				backKeyPressedTime = System.currentTimeMillis();
				showGuide();
				return;
			}
			if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
				finish();
				toast.cancel();
			}
		}
	}

	private void showGuide() {
		toast = Toast.makeText(MainActivity.this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
				Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * 프로필 상세 보기로 이동
	 */
	protected void go_profile_view() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, ProfileViewActivity.class);

		try {
			intent.putExtra("room_id", room_info.getString("room_id"));
			intent.putExtra("male_name", room_info.getString("male_name"));
			intent.putExtra("female_name", room_info.getString("female_name"));
			intent.putExtra("wed_date", room_info.getString("wed_date"));
			intent.putExtra("description", room_info.getString("description"));
			intent.putExtra("img_name1", room_info.getString("profile_img"));
			intent.putExtra("img_name2", room_info.getString("profile_img2"));
			intent.putExtra("img_name3", room_info.getString("profile_img3"));
			intent.putExtra("room_seq", room_seq);
			intent.putExtra("auth", adminAuth);
			intent.putExtra("place", room_info.getString("place"));
			intent.putExtra("wed_time", room_info.getString("wed_time"));
			intent.putExtra("latitude", room_info.getString("latitude"));
			intent.putExtra("longitude", room_info.getString("longtitude"));
			intent.putExtra("card_type", room_info.getString("card_type"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		// finish();
		overridePendingTransition(0, 0);
	}

	private final Runnable getWeddigRoomData = new Runnable() {
		@Override
		public void run() {
			getWeddingRoomData();
		}
	};

	// 앨범 이미지 등록하기
	private final Runnable uploadImage = new Runnable() {
		@Override
		public void run() {
			uploadImage();
		}
	};

	private final Runnable multi_img = new Runnable() {
		@Override
		public void run() {
			multi_img();
		}
	};

	// 이미지 처리
	protected void multi_img() {
		// TODO Auto-generated method stub
		bos = new ByteArrayOutputStream[all_path.length];
		bm = new Bitmap[all_path.length];

		for (int i = 0; i < all_path.length; i++) {

			String photo_path = all_path[i];
			// 이미지 사이즈 화면 사이즈에 맞게 리스케일
			ImageReScale imgReScale = new ImageReScale();
			try {
				bm[i] = imgReScale.loadBackgroundBitmap(
						getApplicationContext(), photo_path);
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 이미지를 상황에 맞게 회전시킨다
			if (bm[i] != null) {

				bm[i] = andUtil.bitmapRotate(photo_path, bm[i]);

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				bos[i] = new ByteArrayOutputStream();
				bm[i].compress(CompressFormat.JPEG, 100, bos[i]);

				try {
					Thread.sleep(200);
					System.out.println("#################");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		handler.sendEmptyMessage(13);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		if (intent.getStringExtra("ROOM_ID") != null) {

			room_id = intent.getStringExtra("ROOM_ID");
			room_seq = intent.getStringExtra("ROOM_SEQ");
			processParsing(mThread, getWeddigRoomData);
		}
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch (msg.what) {
			case 1:
				if (pd != null)
					pd.cancel();
				// 등록 성공

				// Call onRefreshComplete when the list has been refreshed.
				pullToRefreshView.onRefreshComplete();

				show_contents_list();// 리스트 내용 보여주기
				show_profile();// 프로필 정보 보여주기
				show_menu(); // 메뉴 정보 보여주
				show_category();
				
				finishBeforeActivity();
				
				break;
			case 2:
				if (pd != null)
					pd.cancel();
				// 등록 실패

				// AlertDialog dialog =
				// AlertDialogBuilder.pop_ok(LoginActivity.this,
				// "다시 확인해 주세요.").create();
				// dialog.show();

				break;
			case 4:
				if (pd != null)
					pd.cancel();
				// 방 정보 불러오기 실패
				// HttpException에 기재

				// Toast.makeText(getApplicationContext(), "다시 확인해 주세요.",
				// Toast.LENGTH_SHORT).show();
				// finish();
				break;

			case 5:
				if (pd != null)
					pd.cancel();
				// 사진 등록 성공
				processParsing(mThread, getWeddigRoomData);
				break;
			case 6:
				if (pd != null)
					pd.cancel();
				// 사진 등록 실패
				AlertDialog dialog = AlertDialogBuilder.pop_ok(
						MainActivity.this, "다시 확인해 주세요.").create();
				dialog.show();
				break;
			case 7:
				if (pd != null)
					pd.cancel();
				// 사진 등록 실패
					baseDialog("동영상 용량을 초과하였습니다.(100MB)");
				break;
			case 13:
				if (pd != null)
					pd.cancel();

				pop_image_upload();
				break;

			}
		}
	};

	
	// 웨딩룸 정보 가져오기
	protected void getWeddingRoomData() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String, String>();

		// String room_id = User.getUserInfo("ROOM_ID", MainActivity.this);

		data.put("room_id", room_id);
		if (cate_seq != "")
			data.put("cata_seq", cate_seq);

		JSONObject res = MemberModel.loadRoomInfo(data);
		
		if (!isHttpWorthCheck(res)) {
			if (pd.isShowing())
				pd.dismiss();
			return;
		} else {

			try {
				if (res.getString("info").equals("session-out")) {
					reLogin();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		
		try {

			contents = new JSONArray(res.getString("contents"));
			room_info = new JSONObject(res.getString("wed_room_info"));
			cate_list = new JSONArray(res.getString("cateList"));
			user_info = new JSONObject(res.getString("user_info"));
			userId = user_info.getString("u_id");
			adminAuth = res.getString("auth");

			// Thread th = new Thread(test);
			// th.start();
			// distributeUserAndAdmin(adminAuth); //관리자페이지와 유저페이지를 구분하는 곳
			
			if (res.getString("cateSgtList").equals("null")) {
				cate_sgt_list = null;
			} else {
				cate_sgt_list = new JSONArray(res.getString("cateSgtList"));
			}

			handler.sendEmptyMessage(1);

			return;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 이미지 등록
	 */
	protected void uploadImage() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String, String>();
		data.put("wed_seq", room_seq);
		data.put("content", content);
		data.put("cata_seq", cata_seq);
		data.put("FILE_NAME", file_name);
		data.put("card_img_yn", "N");

		JSONObject obj = WeddingModel.procImgUpload(data, selPhoto);
		if (!isHttpWorthCheck(obj)) {
			if (pd.isShowing())
				pd.dismiss();
			return;
		} else {
			try {
				if (obj.getString("info").equals("session-out")) {
					reLogin();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		String info;
		try {
			info = obj.getString("info");
			if (info.equals("ok")) {
				handler.sendEmptyMessage(5);

			} else {
				handler.sendEmptyMessage(6);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 프로필영역 내용 출력
	protected void show_profile() {
		// TODO Auto-generated method stub
		String img_name = "";
		String male_name = "";
		String female_name = "";
		String date = "";
		String time = "";
		String place = "";
		try {

			img_name = room_info.getString("profile_img");
			male_name = room_info.getString("male_name");
			female_name = room_info.getString("female_name");
			date = room_info.getString("wed_date");
			time = room_info.getString("wed_time");
			place = room_info.getString("place");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 프로필 대표이미지
		ImageView iv_profile = (ImageView) header
				.findViewById(R.id.header_profile_img);

		String img_url = Info.MASTER_FILE_URL + "/image/" + img_name;
		AQuery aq = new AQuery(MainActivity.this);
		if (!img_url.contains("null")) {
			aq.id(iv_profile).image(img_url, true, true);
		} else {
			iv_profile.setImageResource(R.drawable.img_card_defult);
		}
		
		
		final String tmp_img_url = img_url;
		iv_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (!tmp_img_url.contains("null")) {
					String img_url = null;
					try {
						img_url = Info.MASTER_FILE_URL + "/image/"
								+ room_info.getString("profile_img");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent(MainActivity.this,
							ViewPagerActivity.class);
					intent.putExtra("IMAGE", img_url);
					startActivity(intent);
					
				} else {
					baseDialog("웨딩 프로필에서 이미지를 등록해주세요.");
				}
				
			}
		});
		
		// 신랑이름
		TextView tv_male_name = (TextView) findViewById(R.id.header_male_name_txt);
		tv_male_name.setText(male_name);
		tv_male_name.setTypeface(CustomTextFont.hunsomsatangR);
		// 신부이름
		TextView tv_female_name = (TextView) findViewById(R.id.header_female_name_txt);
		tv_female_name.setText(female_name);
		tv_female_name.setTypeface(CustomTextFont.hunsomsatangR);
		// 일자
		TextView tv_date = (TextView) findViewById(R.id.header_date_txt);
		int time_hour = Integer.parseInt(time.substring(0, 2));
		if (time_hour > 12) {
			tv_date.setText(date.substring(0, 4) + "." + date.substring(5, 7)
					+ "." + date.substring(8, 10) + " pm" + (time_hour - 12)
					+ ":" + time.substring(2, 4));
//					+ ":" + time.substring(2, 4) + " | " + place);
		} else if(time_hour == 12){
			tv_date.setText(date.substring(0, 4) + "." + date.substring(5, 7)
					+ "." + date.substring(8, 10) + " pm" + (time_hour)
					+ ":" + time.substring(2, 4));
//					+ ":" + time.substring(2, 4) + " | " + place);
		} else {
			if (time_hour < 10) {
				tv_date.setText(date.substring(0, 4) + "."
						+ date.substring(5, 7) + "." + date.substring(8, 10)
						+ " am 0" + time_hour + ":" + time.substring(2, 4));
//						+ " | " + place);
			} else {
				tv_date.setText(date.substring(0, 4) + "."
						+ date.substring(5, 7) + "." + date.substring(8, 10)
						+ " am " + time_hour + ":" + time.substring(2, 4));
//						+ " | " + place);
			}
		}
		
		ImageView hart = (ImageView)findViewById(R.id.icon_hart);
		hart.setVisibility(View.VISIBLE);

	}

	/**
	 * 리스트 내용 보여주기
	 */
	protected void show_contents_list() {
		// TODO Auto-generated method stub
		// 리스트 데이터

		listItem = new ArrayList<Item>();

		int list_cnt = contents.length();
		
		setTabbarScrollAnimation(list_cnt);
		
		for (int i = 0; i < list_cnt; i++) {
			try {
				JSONObject obj = contents.getJSONObject(i);

				Item item = new Item();
				item.con_seq = obj.getString("con_seq");
				item.in_u_seq = obj.getString("in_u_seq");
				item.in_u_id = obj.getString("in_u_id");
				item.wed_seq = obj.getString("wed_seq");
				item.content = obj.getString("content");
				item.file_name = obj.getString("file_name");
				item.like_count = obj.getString("like_count");
				item.cancle_yn = obj.getString("cancle_yn");
				item.u_seq = obj.getString("u_seq");
				item.cmt_cnt = obj.getString("cmt_cnt");
				item.cate_seq = obj.getString("cata_seq");
				item.in_u_nick = obj.getString("in_u_nick");
				item.in_u_picture = obj.getString("in_u_picture");
				item.in_u_sns_picture = obj.getString("in_u_sns_picture");
				item.reg_date = obj.getString("reg_date");
				item.video_yn = obj.getString("video_yn");

				item.img = Info.MASTER_FILE_URL + "/image/" + item.file_name;

				listItem.add(item);

				menu_profile_nick = item.in_u_nick;
				menu_profile_picture = item.in_u_picture;
				menu_profile_sns_picture = item.in_u_sns_picture;
				menu_profile_regdate = item.reg_date;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		String male_name = "";
		String female_name = "";
		try {
			male_name = room_info.getString("male_name");
			female_name = room_info.getString("female_name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//디폴트 이미지 생성 여부
		if(listItem.isEmpty()){
			defualt_img.setVisibility(View.VISIBLE);
		} else {
			defualt_img.setVisibility(View.GONE);
		}
		
		adapter = new ItemAdapter(MainActivity.this, R.layout.row_list,
				listItem, adminAuth, userId, male_name, female_name);
		lv_list.setAdapter(adapter);
		
	}

	// 쇼핑리스트 뿌려주기
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		
		if(resultCode != RESULT_OK){
			return;
		}
		
		
		switch(requestCode){

		case PICK_FROM_MULTI_ALBUM:
			System.out.println("pick_from_album data !!!");
			if (requestCode == PICK_FROM_MULTI_ALBUM && resultCode == Activity.RESULT_OK) {
				all_path = data.getStringArrayExtra("all_path");
				processParsing(mThread, multi_img);
			}

			break;

		case PICK_FROM_ALBUM:
			System.out.println("pick_from_album data !!!");

			try {

				Uri selPhotoUri = Uri.parse(selPhotoPath);

				selPhotoPath = andUtil.getRealPathFromURI(MainActivity.this, selPhotoUri);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;

				if(selPhoto != null){
					selPhoto.recycle();
					selPhoto = null;
				}

				//이미지 사이즈 화면 사이즈에 맞게 리스케일
				ImageReScale imgReScale = new ImageReScale();
				selPhoto = imgReScale.loadBackgroundBitmap(getApplicationContext(), selPhotoPath);	

				setImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case PICK_FROM_CAMERA:
			if(selPhoto != null){
				selPhoto.recycle();
				selPhoto = null;
			}
			singlePicturePlug = true; //사진 찍느것은 싱글 픽쳐이다
	            
			Uri selPhotoUri = MainActivity.imageUri;
			selPhotoPath = andUtil.getRealPathFromURI(MainActivity.this, selPhotoUri);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, MainActivity.imageUri));
			
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			if(selPhoto != null){
				selPhoto.recycle();
				selPhoto = null;
			}
			//이미지 사이즈 화면 사이즈에 맞게 리스케일
			ImageReScale imgReScale = new ImageReScale();
			try {
				selPhoto = imgReScale.loadBackgroundBitmap(getApplicationContext(), selPhotoPath);
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			setImage();
        
			break;
			
		case PICK_FROM_CAPTURE_MOVIE:
			
			mVideoCaptureUri = data.getData();
			videoPath =  andUtil.getRealPathFromURI(MainActivity.this, mVideoCaptureUri);
			
			vedioFile = new File(videoPath);
			
			if(vedioFile.length() > LIMIT_VIDEO_SIZE){
				Message msg = new Message();
				msg.what = 7;
				handler.sendMessage(msg);
				break;
			}
			
			if(videoPath != null  && videoPath.length() > 0)
			{
				getVideoInfo(mVideoCaptureUri);	
			}
			
			pop_video_upload();
			break;
			
		case PICK_FROM_SELECT_VIDEO:
			
			mVideoCaptureUri = data.getData();
			videoPath =  andUtil.getRealPathFromURI(MainActivity.this, mVideoCaptureUri);
			vedioFile = new File(videoPath);
			
			if(vedioFile.length() > LIMIT_VIDEO_SIZE){
				Message msg = new Message();
				msg.what = 7;
				handler.sendMessage(msg);
				break;
			}
			
			if(videoPath != null  && videoPath.length() > 0)
			{
				
				getVideoInfo(mVideoCaptureUri);
			}
			pop_video_upload();
			break;
		}
	}
	
	public static Bitmap curThumb;
	
	private void getVideoInfo( Uri _uri ){  
		
        if (_uri != null) {
        	ContentResolver crThumb = getContentResolver();
            int fileID = Integer.parseInt(_uri.getLastPathSegment());
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inPreferredConfig = Config.RGB_565;
    	    options.inJustDecodeBounds = true;
            Bitmap temp = MediaStore.Video.Thumbnails.getThumbnail(crThumb, fileID, MediaStore.Video.Thumbnails.MINI_KIND, options);

            curThumb = Bitmap.createScaledBitmap(temp, 600, 400, true);
            thumbFilename = _uri.toString();
            
        }
	}
	

	public void setImage() {

		// 미디어 스캐닝
		int version = android.os.Build.VERSION.SDK_INT;

		if (version > 17) {
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.parse("file://"
					+ Environment.getExternalStorageDirectory());
			mediaScanIntent.setData(contentUri);
			sendBroadcast(mediaScanIntent);
		} else {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://"
							+ Environment.getExternalStorageDirectory())));
		}
		try {

			Bitmap tbm = null;
			if (selPhoto != null) {
				// 이미지를 상황에 맞게 회전시킨다
				selPhoto = andUtil.bitmapRotate(selPhotoPath, selPhoto);

				// imageview에 큰 사이트가 들어가면 out of memory가 걸려서 축소시킨다.
				tbm = Bitmap.createScaledBitmap(selPhoto, 200, 200, true);
				// mPhotoImageView.setImageBitmap(tbm);
			} else {

				File file = new File(selPhotoPath);

				if (file.exists()) {

					// 이미지 사이즈 화면 사이즈에 맞게 리스케일
					ImageReScale imgReScale = new ImageReScale();
					selPhoto = imgReScale.loadBackgroundBitmap(
							MainActivity.this, selPhotoPath);
					selPhoto = andUtil.bitmapRotate(selPhotoPath, selPhoto);

					tbm = Bitmap.createScaledBitmap(selPhoto, 200, 200, true);
					// mPhotoImageView.setImageBitmap(tbm);
				}
			}

			file_name = CommonUtil.getFileNameWithoutExtension(selPhotoPath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error" + e);
		}

		// 앨범 내용 작성
		pop_image_upload();

	}

	private final Runnable upload_room_multi_img = new Runnable() {
		@Override
		public void run() {
			upload_room_multi_img();
		}
	};
	
	
	private final Runnable upload_room_video = new Runnable() {
		@Override
		public void run() {
			upload_room_video();
		}
	};
	
	protected void upload_room_multi_img() {
		// TODO Auto-generated method stub

		Map<String, String> data = new HashMap<String, String>();
		data.put("wed_seq", room_seq);
		data.put("content", content);
		data.put("cata_seq", cata_seq);
		data.put("card_img_yn", "N");
		int file_cnt = bos.length;
		for (int i = 0; i < file_cnt; i++) {
			String _file_name = "";
			_file_name = CommonUtil.getFileNameWithoutExtension(all_path[i]);
			data.put("FILE_NAME" + i, _file_name);
		}

		JSONObject obj = AlbumModel.procAlbumImgMultiUpload(data, bos);
		if (!isHttpWorthCheck(obj)) {
			if (pd.isShowing())
				pd.dismiss();
			return;
		} else {
			try {
				if (obj.getString("info").equals("session-out")) {
					reLogin();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			if (obj.getString("info").equals("ok")) {
				handler.sendEmptyMessage(5);
			} else {
				handler.sendEmptyMessage(6);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	// 웨딩룸 여러장 등록
	protected void upload_room_video() {
		// TODO Auto-generated method stub

		Map<String, String> data = new HashMap<String, String>();
		data.put("wed_seq", room_seq);
		data.put("content", content);
		data.put("cata_seq", cata_seq);


		String _imgFile_name = "";
		_imgFile_name = thumbFilename;
		data.put("IMG_FILE_NAME" , _imgFile_name);
		
		String _videoFile_name = "";
		_videoFile_name = CommonUtil.getFileNameWithoutExtension(videoPath);
		data.put("VIDEO_FILE_NAME" , _videoFile_name);
		
		
		
		

		JSONObject obj = AlbumModel.procVideoUpload(data, curThumb, vedioFile);
		if (!isHttpWorthCheck(obj)) {
			if (pd.isShowing())
				pd.dismiss();
			return;
		} else {
			try {
				if (obj.getString("info").equals("session-out")) {
					reLogin();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			if (obj.getString("info").equals("ok")) {
				handler.sendEmptyMessage(5);
			} else {
				handler.sendEmptyMessage(6);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 앨범 사진 내용 작성 팝업
	 */
	private void pop_video_upload() {
		// TODO Auto-generated method stub
		final AlbumWriteDialog pop_write = new AlbumWriteDialog(
				MainActivity.this, MainActivity.this, cate_list);
		// 완료 버튼
		pop_write.btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cata_seq = pop_write.cate_seq;
				content = pop_write.et_content.getText().toString();
				if (content.isEmpty()) {
					AlertDialog dialog = AlertDialogBuilder.pop_ok(
							MainActivity.this, "내용을 입력해 주세요.").create();
					dialog.show();
					return;
				}
				processParsing(mThread, upload_room_video);
				pop_write.dismiss();
			}
		});
		// 그냥 올리기
		pop_write.btn_skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 웨딩룸 안에 앨범 사진등록
				cata_seq = pop_write.cate_seq;
				content = "";
				processParsing(mThread, upload_room_video);
				pop_write.dismiss();
			}
		});
		pop_write.show();
	}

	
	/**
	 * 앨범 사진 내용 작성 팝업
	 */
	private void pop_image_upload() {
		// TODO Auto-generated method stub
		final AlbumWriteDialog pop_write = new AlbumWriteDialog(
				MainActivity.this, MainActivity.this, cate_list);
		// 완료 버튼
		pop_write.btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				cata_seq = pop_write.cate_seq;
				content = pop_write.et_content.getText().toString();

				if (content.isEmpty()) {

					AlertDialog dialog = AlertDialogBuilder.pop_ok(
							MainActivity.this, "내용을 입력해 주세요.").create();
					dialog.show();

					return;
				}
				
				
				if (singlePicturePlug) {
					processParsing(mThread, uploadImage);
					singlePicturePlug = false;
				} else {
					processParsing(mThread, upload_room_multi_img);
				}
	
					

					//비디오 업로드
				
				
				// 웨딩룸 안에 앨범 사진등록
				
				pop_write.dismiss();
			}
		});

		// 그냥 올리기
		pop_write.btn_skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 웨딩룸 안에 앨범 사진등록
				cata_seq = pop_write.cate_seq;
				content = "";
				if (singlePicturePlug) {
					processParsing(mThread, uploadImage);
					singlePicturePlug = false;
				} else {
					processParsing(mThread, upload_room_multi_img);
				}
				pop_write.dismiss();
			}
		});
		pop_write.show();
	}

	@Override
	public void onStart() {
		super.onStart();
		// analytics 분석도구
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		// analytics 분석도구
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		/*
		 * 메뉴 버튼
		 */
		Intent intent = new Intent();
		Class c = null;

		switch (v.getId()) {
		
		case R.id.menu_invite_list_btn: // 관리자 권한

			c = InviteListActivity.class;
			intent.putExtra("WED_SEQ", room_seq);
			intent.putExtra("auth", adminAuth);
			intent.putExtra("u_id", userId);
			break;

		case R.id.menu_profile_btn: // 관리자 권한
			/**
			 * 프로필 상세 보기로 이동
			 */

			menu.showContent();
			go_profile_view();
			return;

		case R.id.menu_facebook_share_btn: // 관리자 권한
			// 임시
			// c = NoticeActivity.class;
			// break;

		case R.id.menu_room_list_btn:
			c = RoomListActivity.class;
			IntroActivity.activity.add(MainActivity.this);
			break;
			
		case R.id.menu_setup_btn:
			c = MainSetupActivity.class;
			IntroActivity.activity.add(MainActivity.this);
			try {
				intent.putExtra("u_sns_picture", user_info.getString("u_sns_picture"));
				intent.putExtra("u_picture", user_info.getString("u_picture"));
				intent.putExtra("u_nick", user_info.getString("u_nick"));
				intent.putExtra("u_id", user_info.getString("u_id"));
				intent.putExtra("sns_yn", user_info.getString("sns_yn"));
				intent.putExtra("room_seq", room_seq);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

		menu.showContent();

		intent.setClass(MainActivity.this, c);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

}
