package mangnani.livestreaming.global.dto;

public interface ResponseMessage {

	//HTTP STATUS 200

	String SUCCESS = "성공";
	String DONATION_SUCCESS = "도네이션 성공";
	String LOGIN_SUCCESS = "로그인 성공";
	String SIGNUP_SUCCESS = "회원가입 성공";
	String PATCH_IMAGE_SUCCESS = "방송국 이미지 변경 성공";
	String PATCH_STATION_DESCRIPTION = "방송국 설명 변경 성공";
	String REISSUE_TOKEN_SUCCESS = "토큰 재발급 성공";

	//HTTP STATUS 400
	String DUPLICATE_LOGIN_ID= "중복된 아이디 입니다.";

	String DUPLICATED_NICKNAME = "중복된 닉네임 입니다.";
	String VALIDATION_FAILED = "인증에 실파하였습니다.";

	String NO_EXISTED_MEMBER = "멤버가 존재하지 않습니다.";

	String NO_EXISTED_STATION = "방송국이 존재하지 않습니다.";

	//HTTP STATUS 401
	String LOGIN_FAILED = "로그인 또는 비밀번호가 올바르지 않습니다";

	String NO_PERMISSION_TOKEN = "권한이 없는 토큰입니다.";

	//HTTP STATUS 500
	String DATABASE_ERROR = "데이터베이스 오류입니다.";

}
