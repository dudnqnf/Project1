package com.takebox.wedding.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.webkit.CookieSyncManager;

import com.takebox.wedding.info.Info;


public class HttpMultiPart {


	public String transfer(String url, Map<String, String> data) throws Exception {

		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
	
			post.setHeader("Cookie", "JSESSIONID="+ (Info.JSESSIONID!=null?Info.JSESSIONID:""));
			System.out.println("http-header : " + "JSESSIONID="+Info.JSESSIONID);
			List params = new ArrayList(); 
			//파라미터 이름, 보낼 데이터 순입니다.
	
			if(data != null){
				Set<String> keys = data.keySet();
				Iterator<String> keyIter = keys.iterator();
	
	
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					params.add(new BasicNameValuePair(key, data.get(key))); 
				}
			}
	
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
					params,HTTP.UTF_8);
			post.setEntity(ent);
	
			System.out.println("executing request " + post.getRequestLine());
	
			HttpResponse responsePOST = Info.httpclient.execute(post);
	
	
			List<Cookie> cookies = ((DefaultHttpClient)Info.httpclient).getCookieStore().getCookies();
			if (!cookies.isEmpty()) {
				for (int i = 0; i < cookies.size(); i++) {
					String cookieString = cookies.get(i).getName() + "=" + cookies.get(i).getValue();
	
					if(cookies.get(i).getName().equals("JSESSIONID")){
						System.out.println("cookieString = " + cookieString);
						Info.JSESSIONID  = cookies.get(i).getValue();
					}
	
					Info.cookieManager.setCookie(url, cookieString);
				}
			}
			CookieSyncManager.getInstance().sync();
			try {
				Thread.sleep(500);	//동기화 하는데 약간의 시간을 필요로 한다.
			} catch (InterruptedException e) {	}
	
	
			HttpEntity resEntity = responsePOST.getEntity();
			String text = EntityUtils.toString(resEntity);
			//Log.i("RESPONSE", text+"");
			return text;
		
		
		} catch (Exception e) {
			// TODO: handle exception
			
			
		}
		return null;
	}



	public String transfer(String url, Map<String, String> data, Bitmap selPhoto) throws Exception {

		try {
			
			Set<String> keys = data.keySet();
			Iterator<String> keyIter = keys.iterator();
	
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				reqEntity.addPart(key, new StringBody((String) data.get(key), "text/plain", Charset.forName("UTF-8") ));
			}
			
			HttpPost httppost = new HttpPost(url);
			
			httppost.setHeader("Cookie", "JSESSIONID="+(Info.JSESSIONID!=null?Info.JSESSIONID:""));
	
			if(selPhoto!=null) {
	
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				selPhoto.compress(CompressFormat.JPEG, 100, bos);
	
				byte[] ba = bos.toByteArray();
				reqEntity.addPart("files", new ByteArrayBody(ba, data.get("FILE_NAME")));
				bos.close();
			}
	
			httppost.setEntity(reqEntity);
	
			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = Info.httpclient.execute(httppost);
	
			HttpEntity resEntity = response.getEntity();
	
			return EntityUtils.toString(resEntity).trim();

		} catch (Exception e) {
			
		}
		return null;
	}
	
	
	public String transfer(String url, Map<String, String> data, Bitmap thumb, File file) throws Exception {

		try {
			
			Set<String> keys = data.keySet();
			Iterator<String> keyIter = keys.iterator();
	
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				reqEntity.addPart(key, new StringBody((String) data.get(key), "text/plain", Charset.forName("UTF-8") ));
			}
			
			HttpPost httppost = new HttpPost(url);
			
			httppost.setHeader("Cookie", "JSESSIONID="+(Info.JSESSIONID!=null?Info.JSESSIONID:""));
	
			if(thumb!=null) {
	
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				thumb.compress(CompressFormat.JPEG, 100, bos);
	
				byte[] ba = bos.toByteArray();
				reqEntity.addPart("files2", new ByteArrayBody(ba, data.get("IMG_FILE_NAME")));
				bos.close();
			}
			
			
//			httppost.setEntity(reqEntity);
			
//			동영상 보내는 부분
			FileInputStream fileInputStream = new FileInputStream(file);
			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			
//			DataOutputStream dataWrite = new DataOutputStream(con.getOutputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while(bytesRead > 0){
				bos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
			}
			fileInputStream.close();
			
			byte[] ba = bos.toByteArray();
			reqEntity.addPart("files", new ByteArrayBody(ba, data.get("VIDEO_FILE_NAME")));
			bos.close();
			
			
			
			
			

			httppost.setEntity(reqEntity);
	
			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = Info.httpclient.execute(httppost);
	
			HttpEntity resEntity = response.getEntity();
	
			return EntityUtils.toString(resEntity).trim();

		} catch (Exception e) {
			
		}
		return null;
	}


	//이미지 여러장 등록
	public String transfer(String url, Map<String, String> data, ByteArrayOutputStream [] files) throws Exception {

		try {
			
			Set<String> keys = data.keySet();
			Iterator<String> keyIter = keys.iterator();
	
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				reqEntity.addPart(key, new StringBody((String) data.get(key), "text/plain", Charset.forName("UTF-8") ));
			}
	
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Cookie", "JSESSIONID="+(Info.JSESSIONID!=null?Info.JSESSIONID:""));
			
	
	
			for(int i=0;i< files.length;i++)
			{	
				if(files[i]==null) continue;
				if(files[i].size() <= 0) continue;
	
				byte[] ba = files[i].toByteArray();
				reqEntity.addPart("files", new ByteArrayBody(ba, data.get("FILE_NAME"+i)));
	
				files[i].flush();
				files[i].close();
			}
	
	
			httppost.setEntity(reqEntity);
	
			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = Info.httpclient.execute(httppost);
	
			HttpEntity resEntity = response.getEntity();
	
			return EntityUtils.toString(resEntity).trim();
		
		} catch (Exception e) {
			
			
			
		}
		return null;
	}
	
}
