package com.takebox.wedding.model;

import java.util.Map;

import org.json.JSONObject;

import com.takebox.wedding.info.Info;
import com.takebox.wedding.util.HttpMultiPart;

public class MemberModel {


	/**
	 * 회원 등록 (이메일)
	 * @return
	 */
	public static JSONObject procMemberJoin(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/userJoin.take", data);
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
	 * 회원 등록 (이메일)
	 * @return
	 */
	public static JSONObject procMemberSNSJoin(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/snsJoin.take", data);
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
	 * 추천 아이디
	 * @return
	 */
	public static JSONObject recom(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/getRecommendRoomId.take", data);
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
	 * 추천 아이디 검색 후 아이디 업데이트
	 * @return
	 */
	public static JSONObject updateId(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		
		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/m_updateRoomInfo.take", data);
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
	 * 회원 로그인
	 * @return
	 */
	public static JSONObject procMemberLogin(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		

		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/j_spring_security_check", data);
			
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
	 * 방정보 초기 값 불러오기
	 * @return
	 */
	public static JSONObject loadRoomInfo(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		
		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/m_mybox_search.take", data);
			System.out.println("res : " + res.trim());
			JSONObject obj = new JSONObject(res.trim());

			return obj;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

	public static JSONObject MemberInfoUpdate(Map<String, String> data){
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
	 * 멤버 로그아웃
	 * @return
	 */
	public static String procMemberLogout(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		
		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/j_spring_security_logout", data);
			System.out.println("res : " + res.trim());

			return res.trim();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 멤버 권한 수정
	 * @return
	 */
	public static JSONObject procUpdateInviteUser(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		
		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/updateInviteUser.take", data);
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
	 * 방에서 내보내기
	 * @return
	 */
	public static JSONObject procDeleteInviteUser(Map<String, String> data){
		HttpMultiPart hmp = new HttpMultiPart();
		
		String res = "";
		try {
			System.out.println("req : " +  data);
			res = hmp.transfer(Info.MASTER_URL+"/deleteInviteUser.take", data);
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