package com.takebox.wedding;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.R;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String tag = "GCMIntentService";
	public static final String SEND_ID = "172466748207";
	
	public GCMIntentService(){ this(SEND_ID); }
	
	public GCMIntentService(String senderId) { super(senderId); }
	
	String[] alrim_info;
	
	private static void generateNotification(Context context, String message) {
		 
		int icon = R.drawable.luncher_icon;
		long when = System.currentTimeMillis();
		 
		 
		NotificationManager notificationManager = (NotificationManager) context
		.getSystemService(Context.NOTIFICATION_SERVICE);
		 
		Notification notification = new Notification(icon, message, when);
		 
		String title = context.getString(R.string.app_name);
		 
		Intent notificationIntent = new Intent(context, MainActivity.class);
		 
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP 
		        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
		notificationIntent, 0);
		 
		 
		 
		notification.setLatestEventInfo(context, title, message, intent);
		 
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 
		notificationManager.notify(0, notification);
		 
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) {
		String msg = intent.getStringExtra("msg");
		Log.e("getmessage", "getmessage:" + msg);
		generateNotification(context,msg);
		
//		Bundle b = intent.getExtras();
//		String title = "";
//		String msg = "";
//		String seq = "";
//		String mode = "";
//		String lang = "";
//		
//		String other_mm_idx = "";
//		String nickname = "";
//		
//		Iterator<String> iterator = b.keySet().iterator();
//		while(iterator.hasNext()) {
//			String key = iterator.next();
//			String value = b.get(key).toString();
//			 //System.out.println( "onMessage. "+key+" : "+value);
//			
//			 if(key.equals("msg")) msg = value;
//			 if(key.equals("seq"))  seq = value;
//			 if(key.equals("mode")) mode = value;
//			 if(key.equals("lang")) lang = value;
//			 
//			 if(key.equals("other_mm_idx")) other_mm_idx = value;
//			 if(key.equals("nickname")) nickname = value;
//			 
//				
//			 
//		}
//		//채팅이면 채팅창인경우는 메시지를 안뿌려준다.
//		if(mode.equals("CHAT"))
//		{
//			ActivityManager mActivityManager = null;
//			mActivityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//			List<ActivityManager.RunningTaskInfo> rt = mActivityManager.getRunningTasks(1);
//			if (rt.size() > 0 )
//			{
//				//if(rt.get(0).topActivity.getClassName().equals("com.tingdong.ChatActivity"))
//				if(rt.get(0).topActivity.getClassName().indexOf("com.tingdong")!=-1)
//				{
//					return;
//				}
//			}
//		}
//		
//		
//		if(lang.equals("ko"))
//    	{
//    		title = "팅동";
//    	} else if(lang.equals("en")){
//    		title = "tingdong";
//    	} else if(lang.equals("zh")) {
//    		title = "叮咚";
//    	} else if(lang.equals("ja")) {
//    		title = "ティンドン";
//    	}
//
//		//메시지박스 보이기
//		Intent i = new Intent(this, GCMAlert.class);
//		i.putExtra("msg", msg);
//		i.putExtra("seq", seq);
//		i.putExtra("mode", mode);
//		i.putExtra("lang", lang);
//		i.putExtra("other_mm_idx", other_mm_idx);
//		i.putExtra("nickname", nickname);
//		PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);
//		try {
//			p.send();
//		} catch (CanceledException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Intent i_intent = new Intent(context, LoadingActivity.class);
//		i_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		
//		i_intent.putExtra("GCM_MM_IDX", other_mm_idx);
//		i_intent.putExtra("GCM_NICKNAME", nickname);
//		i_intent.putExtra("GCM_ZM_IDX", seq);
//		i_intent.putExtra("GCM_AM_MODE", mode);
//		
//		int requestID = (int) System.currentTimeMillis();
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, requestID, i_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		
//		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
//
//		Notification notification = new Notification();
//		notification.icon = R.drawable.ic_launcher;
//		notification.tickerText = title;
//		notification.when = System.currentTimeMillis();
//		//notification.vibrate = new long[] { 500, 100, 500, 100 };
//		//notification.defaults |= Notification.DEFAULT_SOUND;
//		notification.defaults |= Notification.DEFAULT_VIBRATE;
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		
//		notification.sound = Uri.parse("android.resource://"+context.getPackageName()+ "/" + R.raw.push2);
//		
//		
//		notification.setLatestEventInfo(context, title, msg, pendingIntent);
//		notificationManager.notify(0, notification);	
//		
		
		
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(tag, "onError. errorId : "+errorId);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Info.GCM_REG_ID =  regId;
		Log.d(tag, "onRegistered. regId : "+regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.d(tag, "onUnregistered. regId : "+regId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(tag, "onRecoverableError. errorId : "+errorId);
		return super.onRecoverableError(context, errorId);
	}
	
	
	

	
	
}