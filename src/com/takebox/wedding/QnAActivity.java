package com.takebox.wedding;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.takebox.wedding.dialog.AlertDialogBuilder;
import com.takebox.wedding.util.CommonUtil;
import com.takebox.wedding.util.GMailSender;

/**
 * @author sujong
 * 메뉴 - 문의하기 
 */
public class QnAActivity extends Activity {

	FrameLayout backBtn;
	GMailSender sender;
	ProgressDialog dialog;
	EditText question;
	EditText request_email;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.activity_inquire);
	    
	    backBtn = (FrameLayout)findViewById(R.id.btn_back);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		question = (EditText)findViewById(R.id.question);
		request_email = (EditText)findViewById(R.id.request_email);
		
		Button btn = (Button)findViewById(R.id.inquire_send_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//이메일 읿력 확인
				if(question.getText().toString().length() <=0 ){
					//입력 내용 없음 

					AlertDialog dialog = AlertDialogBuilder.pop_ok(QnAActivity.this, "내용을 입력하세요.").create();    // 알림창 객체 생성
					dialog.show();   
					return;
				}
				
				//이메일 읿력 확인
				if(request_email.getText().toString().length() <=0 ){
					//입력 내용 없음 

					AlertDialog dialog = AlertDialogBuilder.pop_ok(QnAActivity.this, "이메일을 입력하세요.").create();    // 알림창 객체 생성
					dialog.show();   
					return;
				}


				//이메일 유효 확인
				if(!CommonUtil.isValidEmail(request_email.getText().toString())){
					AlertDialog dialog = AlertDialogBuilder.pop_ok(QnAActivity.this, "이메일 형식에 맞지 않습니다.").create();    // 알림창 객체 생성
					dialog.show();    
					return;
				}
				
				sender = new GMailSender("takebox.co.kr@gmail.com", "1xpdlzmqkrtm"); // SUBSTITUTE ID PASSWORD
				timeThread();
			}
		});
  		
	}
	
	public void timeThread() {

		dialog = new ProgressDialog(this);
		dialog = new ProgressDialog(this);
		dialog.setTitle("Loading...");
		dialog.setMessage("문의사항을 보내는 중입니다.");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				try {
					sender.sendMail("문의사항", // subject.getText().toString(),
							"응답받을 이메일 : " + request_email.getText().toString() + "\n" +
							"문의사항 : " + question.getText().toString(), // body.getText().toString(),
							request_email.getText().toString(), // from.getText().toString(),
							"help@takebox.co.kr" // to.getText().toString()
					);
				} catch (Exception e) {
					Log.e("SendMail", e.getMessage(), e);
				}
				dialog.dismiss();
				handler.sendEmptyMessage(1);
			}
		}).start();
		
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch(msg.what) {

			case 1:

				Toast.makeText(QnAActivity.this, "문의사항이 전달되었습니다. 감사합니다.", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onStop() {
		super.onStop();
		//analytics 분석도구 
		EasyTracker.getInstance(this).activityStop(this);  // Add this method.

	}


}
