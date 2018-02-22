package com.takebox.wedding;

import java.util.ArrayList;

public class BioData {
	String bio;
	int radio;
	
	public BioData(String bio){
		this.bio = bio;
		this.radio = R.drawable.btn_radio;
	}
	
	public BioData(String bio, int drawable){
		this.bio = bio;
		this.radio = drawable;
	}
	
	public static ArrayList<BioData> get_bio(){
		ArrayList<BioData> list = new ArrayList<BioData>();
		for(int i=0;i<bios.length;i++)
			list.add(new BioData(bios[i]));
		return list;
	}
	
	public static ArrayList<BioData> get_bio(int position, int drawable){
		ArrayList<BioData> list = new ArrayList<BioData>();
		for(int i=0;i<bios.length;i++)
			list.add(new BioData(bios[i]));
		
		list.set(position, new BioData(BioData.bios[position], drawable));
		return list;
	}
	
	public static String[] bios = new String[]{
		"여기 두 사람\n" +
		"소중한 순간들을 사랑으로 엮어\n" +
		"이제 여러 어른과 친지 앞에서 혼인의 예를 갖추려합니다.\n" +
		"더 넓고 깊은 사랑이 될 수 있도록\n" +
		"가까이에서 축하 해 주시면 더 없는 영광이겠습니다.",
		
		"결혼은 또 하나의 새로운 인생의 출발이라고 합니다.\n" +
		"오늘이 있기까지 많은 사랑과 관심을 기울여 주시고 이끌어 주신 여러 어른들과\n" +
		"친지분들을 모시고 저희 두사람이 배년해로의 진실한 가약을 맺고자 합니다.\n" +
		"부디 참석하시어 저희가 내딛는 새 인생의 첫걸음을\n" +
		"다사로움과 축복으로 빛내 주시기 바랍니다.",
		
		"새로운 마음과 새 의미를 간직하며\n" +
		"저희 두 사람이 새 출발의 첫 걸음을 내딛습니다.\n" +
		"좋은 꿈, 바른 뜻으로 올바르게 살 수 있도록 축복과 격려주시면\n" +
		"더없는 기쁨으로 간직하겠습니다.",
		
		"항상 귀댁에 평안과 행복이 가득하시기를 기원합니다.\n" +
		"아뢰올 말씀은 평소 저희를 아껴주시고 격려해 주시던 여러 어른과\n" +
		"친지분들을 모시고 결혼식을 올리고자 하오니 오셔서 저희들의\n" +
		"새 출발을 축복해 주시기 바랍니다.",
		
		"저희 두 사람이\n" +
		"이제 믿음과 사랑으로\n" +
		"한 가정을 이루게 되었습니다.\n" +
		"부디 오셔서 축복해 주시기 바랍니다.",
		
		"부족하지 않되\n" +
		"지나치지 말라는 말씀\n" +
		"마음속 깊이 간직하고 있습니다.\n" +
		"오늘 저희 두 사람\n" +
		"평소 존경해온 어른, 친지분들 앞에서\n" +
		"변년의 길을 가옵니다.\n" +
		"오셔서 앞으로도\n" +
		"삶의 참의미를 깨달아 가도록\n" +
		"일깨워 주소서.",
		
		"여러분의 지극한 정성과\n" +
		"사랑으로 성장한 두 사람이\n" +
		"성스러운 촛불을 밝히게 되었습니다.\n" +
		"바쁘신 중이라도 부디 참석하셔서\n" +
		"결혼의 첫 걸음을 힘차게 내디딜 수 있도록\n" +
		"축볼과 격려를 해 주시면 감사하겠습니다.",
		
		"여기 두 사람 다 자랐다지만\n" +
		"행여 자신에게, 남에게 그릇될까\n" +
		"새삼 염려스럽습니다.\n" +
		"바쁘시더라도 부디 오셔서 의롭고 슬기롭게 살아가는 법\n" +
		"가르쳐 주십시오",
		
		"저희 아들과 딸이\n" +
		"사랑과 믿음으로 한 가정을 이루고저\n" +
		"혼인의 예를 올리게 되었습니다." +
		"부디 오셔서 이들의 뜻깊은 새 출발을\n" +
		"축복해 주시면 감사하겠습니다.",
		
		"두 사람이 사랑으로 만나\n" +
		"진실과 이해로써 하나를 이루려 합니다.\n" +
		"이 두 사람을 지성으로 아끼고 돌봐주신 여러 어른과 친지를 모시고\n" +
		"서약을 맺고자 하오니 바쁘신 가운데 두 사람의 장래를\n" +
		"가까이에서 축복해 주시면 고맙겠습니다."
		
	};
}