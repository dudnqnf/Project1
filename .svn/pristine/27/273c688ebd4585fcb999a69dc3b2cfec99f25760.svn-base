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
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;
import com.kakao.AppActionBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.kakao.AppActionInfoBuilder;
import com.takebox.wedding.R;

/**
 * 텍스트, 이미지, 링크, 버튼 타입으로 메시지를 구성하여 카카오톡으로 전송한다.
 */
public class KakaoLinkAppRecomActivity extends Activity {
    private KakaoLink kakaoLink;
    private Spinner text, link, image, button;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private final String imageSrc = "http://file.takebox.co.kr/logo.jpg";
    private final String weblink = "http://www.kakao.com/services/8";

    /**
     * 메시지를 구성할 텍스트, 이미지, 링크, 버튼을 위한 spinner를 구성한다.
     * 메시지 전송 버튼과 메시지 다시 구성하기 버튼을 만든다.
     * @param savedInstanceState activity 내려가지 전에 저장한 객체
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                kakaoTalkLinkMessageBuilder.addImage(imageSrc, 120, 120);
                kakaoTalkLinkMessageBuilder.addText("\n\n[웨딩박스]" +
                		"\n둘만의 결혼식이 아닌, 모두가 함께 만들어가는 소셜 웨딩 앨범 웨딩박스" +
                		"\n\n지금 시작해보세요!");

     // 앱이 설치되어 있는 경우 kakao<app_key>://kakaolink?execparamkey1=1111 로 이동. 앱이 설치되어 있지 않은 경우 market://details?id=com.kakao.sample.kakaolink&referrer=kakaotalklink 또는 https://itunes.apple.com/app/id12345로 이동
     //       if (linkType.equals(getString(""))){
//                kakaoTalkLinkMessageBuilder.addAppLink("Welcome to KakaoLinkSample~",
//                    new AppActionBuilder()
//                        .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("referrer=kakaotalklink").build())
//                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
//                );
     //       }
     // 웹싸이트에 등록한 "http://www.kakao.com"을 overwrite함. overwrite는 같은 도메인만 가능.
     //       else if (linkType.equals(getString(string.use_weblink))) {
     //           kakaoTalkLinkMessageBuilder.addWebLink(getString(string.kakaolink_weblink), weblink);
    //        }

            // 웹싸이트에 등록된 kakao<app_key>://kakaolink로 이동
//            if (buttonType.equals(getString(string.use_appbutton)))
                kakaoTalkLinkMessageBuilder.addAppButton("웨딩박스로 이동");
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
