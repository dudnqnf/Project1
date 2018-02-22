package com.takebox.wedding.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Bitmap;

import com.takebox.wedding.info.Info;
import com.takebox.wedding.util.HttpMultiPart;

public class AlbumModel {

	/**
	 * 좋아요'
	 * @return
	 */
	public static JSONObject procLike(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/updateLike.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 댓글가져오기
	 * @return
	 */
	public static JSONObject procGetRepleList(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/getComment.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	
	/**
	 * 앨범 상세 가져오기(이미지, 내용, 댓글, 좋아요)
	 * @return
	 */
	public static JSONObject procGetAlbumDetail(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/getWedContentDetail.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	
	/**
	 * 댓글가져오기
	 * @return
	 */
	public static JSONObject procWriteReple(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/saveComment.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	

	/**
	 * 앨범 사진 여러장 업로드
	 * @return
	 */
	public static JSONObject procAlbumImgMultiUpload(Map<String, String> data, ByteArrayOutputStream[] bos){
		HttpMultiPart hmp = new HttpMultiPart();

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/wedPictureUpload.take", data, bos);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 동영상 등록(업로드)
	 * @return
	 */
	public static JSONObject procVideoUpload(Map<String, String> data, Bitmap bm, File file){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/wedVideoUpload.take", data, bm, file);
//			res = hmp.transfer(Info.MASTER_URL+"/wedVideoUpload.take", data, file);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	
	
	
	/**
	 * 컨텐츠 지우기
	 * @return
	 */
	public static JSONObject procDelContent(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/delContent.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 컨텐츠 수정하기
	 * @return
	 */
	public static JSONObject procEditContent(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/updateWedContent.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 청첩장종류 가져오기
	 * @return
	 */
	public static JSONObject procGetCardType(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/getCardType.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 청첩장적용
	 * @return
	 */
	public static JSONObject procWedRoomProfileUpdate(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/wedRoomProfileUpdate.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 청첩장구매
	 * @return
	 */
	public static JSONObject procInsertBuyItem(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/insertBuyItem.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 구입한 청첩장 불러오기
	 * @return
	 */
	public static JSONObject procGetBuyItems(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/getBuyItems.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
}
