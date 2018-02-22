package com.takebox.wedding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.info.User;
import com.takebox.wedding.model.WeddingModel;

public class JoinSetupBasicActivity extends HttpExceptionActivity {

	public static final int NAVERMAP = 1;
	private Thread mThread = null;

	private EditText et_male_name;
	private EditText et_female_name;

	private EditText et_place;
	private Button map;


	private String date; //날짜
	private String time; //시간

	private String place; //장소

	private String _room_id, _wed_seq;
	private double latitude = 37.5666091;
	private double longitude = 126.978371;
	private String naver_place;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_join_setup_basic);

		//신부 신랑 이름 입력 
		et_male_name = (EditText)findViewById(R.id.basic_male_name_edit);
		et_female_name = (EditText)findViewById(R.id.basic_female_name_edit);

		


		//결혼식 일자 입력
		Button btn_date = (Button)findViewById(R.id.basic_date_btn);
		btn_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//날짜 다이얼로그 
				DialogDatePicker();
			}
		});


		//시간 입력 선택  버튼
		Button btn_time = (Button)findViewById(R.id.basic_time_btn);
		btn_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//시간 다이얼로그
				DialogTimePicker();

			}
		});



		//다음버튼
		Button btn_ok = (Button)findViewById(R.id.basic_next_btn);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				//신랑이름 입력 확인
				if(et_male_name.getText().toString().length() <=0 ){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(JoinSetupBasicActivity.this, "신랑이름을 입력해 주세요.").create();    // 알림창 객체 생성
					dialog.show();  
					return;
				}  

				//신부이름 입력 확인
				if(et_female_name.getText().toString().length() <=0 ){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(JoinSetupBasicActivity.this, "신부이름을 입력해 주세요.").create();    // 알림창 객체 생성
					dialog.show();  
					return;
				}

				//날짜 확인
				if(date == null || date.length() <=0 ){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(JoinSetupBasicActivity.this, "결혼식 날짜를 입력해 주세요.").create();    // 알림창 객체 생성
					dialog.show();  
					return;
				}
				
				//시간 확인
				if(time == null || time.length() <=0 ){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(JoinSetupBasicActivity.this, "결혼식 시간을 입력해 주세요.").create();    // 알림창 객체 생성
					dialog.show();  
					return;
				}



				//통신
				processParsing(mThread, weddingbox_write);

			}
		});
		
		//장소
		et_place = (EditText)findViewById(R.id.basic_place_edit);
		map = (Button)findViewById(R.id.naver_map);
		map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JoinSetupBasicActivity.this, NaverMapActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("flag", "edit");
				startActivityForResult(intent, NAVERMAP);
				overridePendingTransition(0, 0);	
			}
		});
		
		goToInitialAppInfo(InitialAppInfoActivity.kindOfActivity[2]);
	}




	//시간 선택 다이얼로그
	protected void DialogTimePicker() {
		// TODO Auto-generated method stub
		TimePickerDialog.OnTimeSetListener mTimeSetListener = 
				new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				String _time = ""+String.format("%02d", hourOfDay) + String.format("%02d", minute);
				System.out.println("time = " + _time);

				time = _time;
				//버튼에 시간 출력
				TextView timeview = (TextView)findViewById(R.id.basic_time_btn);
				timeview.setText(time.substring(0, 2) + " : " + time.substring(2, 4));
			}
		};
		TimePickerDialog alert = new TimePickerDialog(this, 
				mTimeSetListener, 12, 0, true);
		alert.show();
	}

	private final Runnable weddingbox_write = new Runnable() {
		@Override
		public void run() {
			weddingbox_write();
		}
	};

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:
				if(pd!=null) pd.cancel();
				//등록 성공
//				Toast.makeText(getApplicationContext(), "생성되었습니다", Toast.LENGTH_SHORT).show();
				//생성받은 아이디 저장
				User.setUserInfo("ROOM_ID", _room_id, JoinSetupBasicActivity.this);
				User.setUserInfo("ROOM_SEQ", _wed_seq, JoinSetupBasicActivity.this);

				//셋업 프로필로 이동 
				go_setup_profile();

				break;
			case 2:
				if(pd!=null) pd.cancel();
				//등록 실패

				AlertDialog dialog = AlertDialogBuilder.pop_ok(JoinSetupBasicActivity.this, "다시 확인해 주세요.").create();    // 알림창 객체 생성
				dialog.show();   

				break;

			}
		}
	};


	//셋업 프로필로 이동
	protected void go_setup_profile() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(JoinSetupBasicActivity.this, JoinSetupProfileActivity.class);
		intent.putExtra("ROOM_ID", _room_id); //웨딩룸 이름
		intent.putExtra("ROOM_SEQ", _wed_seq); //웨딩룸 번호

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
		overridePendingTransition(0, 0);	
	}

//	protected String ksc2uni(String str) throws UnsupportedEncodingException {
//		return new String( str.getBytes("KSC5601"), "8859_1");
//		}


	/**
	 * 웨딩박스 만들기 
	 */
	protected void weddingbox_write() {
		// TODO Auto-generated method stub
		Map<String, String> data = new HashMap<String,String>();

		
		String _male = et_male_name.getText().toString();
		String _female = et_female_name.getText().toString();
		place = et_place.getText().toString();

		data.put("male_name",_male);
		data.put("female_name", _female);
		data.put("wed_date", date);
		
		
		data.put("wed_time", time);
		data.put("place", place);
		data.put("latitude", latitude+"");
		data.put("longtitude", longitude+"");


		JSONObject obj = WeddingModel.procWeddingMake(data);
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
			if(info.equals("생성되었습니다")){
				//성공

				String room_id = obj.getString("loc");
				String wed_seq = obj.getString("wed_seq");

				_room_id = room_id;
				_wed_seq = wed_seq;

				handler.sendEmptyMessage(1);
				return;

			}else if(info.equals("fail")){
				//실패
				handler.sendEmptyMessage(2);
				return;
			}


			handler.sendEmptyMessage(2);
			return;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//일자 다이얼로그
	private void DialogDatePicker(){
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog.OnDateSetListener mDateSetListener = 
				new DatePickerDialog.OnDateSetListener() {
			// onDateSet method
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				String date_selected = String.valueOf(year)+"-"+String.format("%02d", monthOfYear+1)+
						"-"+String.format("%02d", dayOfMonth);

				date = date_selected ;
				//버튼에 일자 출력
				TextView dateview = (TextView)findViewById(R.id.basic_date_btn);
				dateview.setText(date);
				System.out.println(date_selected);
			}
		};
		DatePickerDialog alert = new DatePickerDialog(this,  mDateSetListener,  
				cyear, cmonth, cday);
		alert.show();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode != RESULT_OK){
			return;
		}
		
		
		switch(requestCode){

		case NAVERMAP:
			
			latitude = data.getDoubleExtra("latitude", 37.5666091);
			longitude = data.getDoubleExtra("longitude", 126.978371);
			naver_place = data.getStringExtra("place");
			Log.i("data", latitude + ", " + longitude + ", " + place);
			break;
		}
	}
	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		Toast.makeText(JoinSetupBasicActivity.this, "방설정을 마쳐주세요.", Toast.LENGTH_SHORT).show();
//	}

}
