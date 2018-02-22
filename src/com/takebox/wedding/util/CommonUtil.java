package com.takebox.wedding.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class CommonUtil {	 


	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;


	public static String nvl(String str, String val) {
		if (str == null || str.equals("") || str.equals("null"))
			return val;
		else
			return str;
	}

	public static String nvl(String str) {
		return nvl(str, "");
	}

	public static String nvl(Object o, String val) {
		if (o == null)
			return val;
		else
			return nvl((String) o, val);
	}

	public static String nvl(Object o) {
		return nvl(o, "");
	}

	public static String nvl(String str, String val, int t) {
		String param = nvl(str, val);
		return nvl(nvlTypeDecision(param, t), val);
	}

	public static String nvl(String str, int t) {
		String param = nvl(str);
		return nvlTypeDecision(param, t);
	}

	public static String nvl(Object o, int t) {
		String param = nvl(o);
		return nvlTypeDecision(param, t);
	}

	public static String nvl(Object o, String val, int t) {
		String param = nvl(o, val);
		return nvl(nvlTypeDecision(param, t), val);
	}

	public static String nvlTypeDecision(String str, int t) {
		if (t == 1)
			return numberOnly(str);
		if (t == 2)
			return alphabetOnly(str);
		if (t == 3)
			return alphabetNumberOnly(str);
		if (t == 4)
			return replaceSpecialChar(str);
		if (t == 5)
			return urlContainsParam(str);
		return str;
	}

	public static String numberOnly(String str) {
		return str.replaceAll("[^0-9]", "");
	}

	public static String alphabetOnly(String str) {
		return str.replaceAll("[^a-zA-Z]", "");
	}

	public static String alphabetNumberOnly(String str) {
		return str.replaceAll("[^0-9a-zA-Z]", "");
	}

	public static String replaceSpecialChar(String str) {
		String str1 = str.replaceAll("[<]", "&lt");

		str1 = str1.replaceAll("[>]", "&gt");
		str1 = str1.replaceAll("[\"]", "&quot;");
		str1 = str1.replaceAll("[#]","");
		str1 = str1.replaceAll("[|]", "&#124;");
		str1 = str1.replaceAll("[$]", "&#36;");
		str1 = str1.replaceAll("[%]", "&#37;");
		str1 = str1.replaceAll("[']", "&#39;");
		str1 = str1.replaceAll("[/]", "&#47;");
		str1 = str1.replaceAll("[(]", "&#40;");
		str1 = str1.replaceAll("[)]", "&#41;");
		str1 = str1.replaceAll("[,]", "&#44;");
		str1 = str1.replaceAll("[&]","&amp;");

		str1 = str1.replaceAll("[;]","");
		str1 = str1.replaceAll("[:]","");
		str1 = str1.replaceAll("[+]","");
		//str1 = str1.replaceAll("[-]","");
		str1 = str1.replaceAll("[=]","");
		str1 = str1.replaceAll("[`]","");


		return str1;
	}

	public static String urlContainsParam(String str) {
		str = str.replaceAll("\\[", "");
		str = str.replaceAll("\\]", "");
		str = str.replaceAll("~", "");
		str = str.replaceAll("[<>,.|/{}:;!@#$%^&*()_+=-`'\"]", "");

		return str;
	}


	public static JSONObject getJSON(String returnString) throws JSONException
	{

		JSONObject itemObj = new JSONObject(returnString);
		JSONArray data = (JSONArray)itemObj.get("data");
		itemObj = (JSONObject)data.get(0);

		return itemObj;
	}


	public static JSONArray getJSONArray(String returnString) throws JSONException
	{
		JSONObject itemObj = new JSONObject(returnString);
		JSONArray data = (JSONArray)itemObj.get("data");

		return data;
	}


	public static String getContentUrl(String urlString) {

		HttpURLConnection httpConn = null;
		BufferedReader in = null;
		String inputLine = null;		
		StringBuffer sb = new StringBuffer();

		try {
			URL url = new URL(urlString);

			httpConn = (HttpURLConnection)url.openConnection();
			int responseCode = httpConn.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK){
				in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"EUC-KR"));

				while ((inputLine = in.readLine()) != null) {
					sb.append(inputLine);
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{if(in != null) in.close();in=null;} catch(Exception e){System.out.println(e);}
			try{if(httpConn != null) httpConn.disconnect();httpConn=null;} catch(Exception e){System.out.println(e);}				
		}
		return sb.toString();
	}



	public static String getHttpsContentUrl(String urlString) {

		HttpsURLConnection httpConn = null;
		BufferedReader in = null;
		String inputLine = null;		
		StringBuffer sb = new StringBuffer();

		try {
			URL url = new URL(urlString);

			httpConn = (HttpsURLConnection)url.openConnection();
			int responseCode = httpConn.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK){
				in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));

				while ((inputLine = in.readLine()) != null) {
					sb.append(inputLine);
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{if(in != null) in.close();in=null;} catch(Exception e){System.out.println(e);}
			try{if(httpConn != null) httpConn.disconnect();httpConn=null;} catch(Exception e){System.out.println(e);}				
		}
		return sb.toString();
	}



	public static String getURL(String URL, String query ) {
		BufferedReader in = null;
		StringBuffer Rvalue = new StringBuffer();

		try {
			URL url = new URL(URL);
			HttpURLConnection http = null;

			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				http = https;
			} else {
				http = (HttpURLConnection) url.openConnection();
			}
			http.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(http.getOutputStream());
			wr.write(query);
			wr.flush();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream(),"EUC-KR"));

			String line;

			while ((line = bufferedReader.readLine()) != null)
			{

				//line = URLDecoder.decode(line,"EUC-KR");
				Rvalue.append(line + "\n");
			}

			wr.close();
			bufferedReader.close();


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		return Rvalue.toString();
	}



	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain,
					String authType)
							throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain,
					String authType)
							throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub

			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
			.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};



	public static String convertToStringRepresentation(final long value){
		final long[] dividers = new long[] { T, G, M, K, 1 };
		final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
		if(value < 1)
			throw new IllegalArgumentException("Invalid file size: " + value);
		String result = null;
		for(int i = 0; i < dividers.length; i++){
			final long divider = dividers[i];
			if(value >= divider){
				result = format(value, divider, units[i]);
				break;
			}
		}
		return result;
	}

	private static String format(final long value,
			final long divider,
			final String unit){
		final double result =
				divider > 1 ? (double) value / (double) divider : (double) value;
				return new DecimalFormat("#,##0.#").format(result) + " " + unit;
				//return new DecimalFormat("#,##0.#").format(result);
	}

	public final static boolean isValidEmail(CharSequence target) {
		try {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
		catch( NullPointerException exception ) {
			return false;
		}
	}



	/**
	 * 이메일 유효성 체크
	 * @param email
	 * @return
	 */

	public static boolean isValidEmail(String email)
	{
		Pattern p = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(email);
		return m.matches();
	}







	/**
	 * 비밀번호 유효성 체크
	 * @param pwd
	 * @return
	 */
	public static boolean isValidPassWord(String pwd)
	{	//비밀번호 유효성 체크
		//		3. 영수문자(알파벳과 숫자 각각 최소 1개 이상)로 구성된 6~12자리 문자열에 일치되는 정규식
		//
		//			^(?=([a-zA-Z]+[0-9]+[a-zA-Z0-9]*|[0-9]+[a-zA-Z]+[a-zA-Z0-9]*)$).{6,12}
		Pattern p = Pattern.compile("^(?=([a-zA-Z]+[0-9]+[a-zA-Z0-9]*|[0-9]+[a-zA-Z]+[a-zA-Z0-9]*)$).{6,20}");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	
	
	/**
	 * 
	 * 파일 이름 + 확장자 추출
	 * @param fileName
	 * @return
	 */
	public static String getFileNameWithoutExtension(String fileName) {       
		File tmpFile = new File(fileName);        
		tmpFile.getName();        
		int whereDot = tmpFile.getName().lastIndexOf('.');        
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2 ) {           
			
			String extension = fileName.substring(whereDot+1);    
			
			return tmpFile.getName().substring(0, whereDot)+"."+extension;           
		}           
		return "";
	}


}

