package com.takebox.wedding.info;

import com.facebook.android.Facebook;

public class FacebookInfo {

	
	public static final int REQ_CODE_FACEBOOK_LOGIN = 1001;

	public static boolean FacebookLogin = false;
	public static boolean RetryLogin = false;

	public static Facebook FacebookInstance = null;

	public static String[] FACEBOOK_PERMISSIONS = {"publish_stream", "read_stream", "user_photos", "email"};

	public static String FACEBOOK_ACCESS_TOKEN = "";
	public static String FACEBOOK_APP_ID = "720511661350022";
	public static String FACEBOOK_API_KEY = "db72968f7022871f6c4e3eea44d7b8b9"; //app secret key
	
}
