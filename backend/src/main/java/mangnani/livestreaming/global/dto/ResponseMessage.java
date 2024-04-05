package mangnani.livestreaming.global.dto;

public interface ResponseMessage {

	//HTTP STATUS 200
	String SUCCESS = "Success";

	//HTTP STATUS 400
	String DUPLICATE_LOGIN_ID= "중복된 아이디 입니다.";

	String DUPLICATED_NICKNAME = "중복된 닉네임 입니다.";

	//HTTP STATUS 401
	String LOGIN_FAILED = "로그인 또는 비밀번호가 올바르지 않습니다";

}
