/**
 * Copyright 2014 Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.takebox.wedding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;
import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.takebox.wedding.info.Info;
import com.takebox.wedding.util.Time;
import com.takebox.wedding.R;

/**
 * 텍스트, 이미지, 링크, 버튼 타입으로 메시지를 구성하여 카카오톡으로 전송한다.
 */
public class KakaoLinkActivity extends Activity {
    private KakaoLink kakaoLink;
    private Spinner text, link, image, button;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private final String weblink = Info.MASTER_URL;
    private String room_id;
    private String room_no;
    private String img_name;
    private String img_url = Info.DEFAULT_IMG;
    private String description;
    String date = "";
    String place = "";
    String time = "";

    /**
     * 메시지를 구성할 텍스트, 이미지, 링크, 버튼을 위한 spinner를 구성한다.
     * 메시지 전송 버튼과 메시지 다시 구성하기 버튼을 만든다.
     * @param savedInstanceState activity 내려가지 전에 저장한 객체
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i =getIntent();
        room_id = i.getStringExtra("room_id");
        room_no = i.getStringExtra("room_no");
        img_name = i.getStringExtra("img_name");
        
        
        if(i.getStringExtra("date") != null)
        	date = i.getStringExtra("date");
        
        
        if(i.getStringExtra("time") != null)
        	time = i.getStringExtra("time");
        
        
        if(i.getStringExtra("place") != null)
        	place = i.getStringExtra("place");
        
        if(!img_name.equals("null") && !img_name.equals("")){
        	img_url = Info.MASTER_FILE_URL + "/image/resize.php?w=400&h=300&img=" + img_name;
        }
        
        if(i.getStringExtra("description") != null){
        	description = i.getStringExtra("description");
        }
        	
        
        try {
            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            sendKakaoTalkLink();
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        
        } catch (KakaoParameterException e) {
            alert(e.getMessage());
        }
        
        
   
    }

    private void sendKakaoTalkLink() {
        try {
                kakaoTalkLinkMessageBuilder.addImage(img_url+"", 400, 300);
                kakaoTalkLinkMessageBuilder.addText("" +
                		"\n" + description +
                		"\n\n웨딩박스ID : " +room_id+
                		"\n날짜 : " + Time.calculateDate(date) +
                		"\n시간 : " + Time.calculateTime(time) +
                		"\n장소 : " + place +
                		"\n\n웨딩박스 앱에서 ID를 입력해주시면, 더 많은 웨딩사진을 보실 수 있어요." +
                		"\n꼭 참석해 주세요." +
                		"\nhttp://m.takebox.co.kr/invite/"+room_id);

     // 앱이 설치되어 있는 경우 kakao<app_key>://kakaolink?execparamkey1=1111 로 이동. 앱이 설치되어 있지 않은 경우 market://details?id=com.kakao.sample.kakaolink&referrer=kakaotalklink 또는 https://itunes.apple.com/app/id12345로 이동
//            if (linkType.equals(getString(""))){
//                kakaoTalkLinkMessageBuilder.addAppLink("Welcome to KakaoLinkSample~",
//                    new AppActionBuilder()
//                        .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("referrer=kakaotalklink").build())
//                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
//                );
//            }
     // 웹싸이트에 등록한 "http://www.kakao.com"을 overwrite함. overwrite는 같은 도메인만 가능.
     //       else if (linkType.equals(getString(string.use_weblink))) {
//                kakaoTalkLinkMessageBuilder.addWebLink("http://m.takebox.co.kr/invite/"+room_id, weblink);
    //        }

            // 웹싸이트에 등록된 kakao<app_key>://kakaolink로 이동
//            if (buttonType.equals(getString(string.use_appbutton)))
                kakaoTalkLinkMessageBuilder.addAppButton("웨딩사진보기", new AppActionBuilder()
                .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("room_no="+room_no+"&room_id="+room_id).setMarketParam("referrer=kakaotalklink").build())
                .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build());
//            // 웹싸이트에 등록한 "http://www.kakao.com"으로 이동.
//            else if (buttonType.equals(getString(string.use_webbutton)))
//                kakaoTalkLinkMessageBuilder.addWebButton(getString(string.kakaolink_webbutton), null);

        	kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
        	
        	finish();
        } catch (KakaoParameterException e) {
            alert(e.getMessage());
        }
    }

    private void alert(String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
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
