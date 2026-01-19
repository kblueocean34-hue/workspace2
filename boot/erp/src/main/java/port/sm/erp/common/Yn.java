package port.sm.erp.common;

//DB표현(Y/N)과 비즈니스 표현(boolean)을 분리하기 위해 만든다
//오라클이기 때문에 DB USE_YN CHAR(1) Y / N 
//프론트 / 비즈니스 로직 useYn true / false
//을 만들때 마다 서비스 하고 응답하고 반복을 한다
//역할분리
//“아… 이 프로젝트 Y/N 여기저기 박혀 있네…다 바꿔야겠네… 😇”
public final class Yn {//이코드에 목적 불리언과 문자열 YN서로 변환해 주는 유틸리티
	
	private Yn() {//final 상속금지
		//유틸 클래스는 생성자를 막는다
	} //객체를 만드는 것이 아니기 때문에 생성자를 막는다는 표시
	
	public static String toYn(boolean value) {
		return value ? "Y" : "N"; //조건 ? 참일때값 : 거짓일때 값
	}
	
	public static boolean toBool(String yn) {
		return "Y".equalsIgnoreCase(yn);
		//대소문자를 무시하고 비교 y : Y
	}
	
	//오라클에는 불리언이 없기 때문에 Y/N사용 DB는 Y 자바는 boolean active

}
