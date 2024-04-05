package mangnani.livestreaming.global.dto;

public interface ResponseCode {

	//HTTP STATUS 200
	String LOGIN_SUCCESS = "LS";
	String DONATION_SUCCESS = "DS";

	//HTTP STATUS 201
	String SIGN_UP_SUCCESS = "SUS";

	//HTTP STATUS 400
	String DUPLICATED_LOGIN_ID = "DLI";
	String DUPLICATED_NICKNAME = "DN";

	String NO_EXISTED_MEMBER = "NEM";

	String VALIDATION_FAILED = "VF";

	//HTTP STATUS 401
	String LOGIN_FAILED = "LF";

	String NO_PERMISSION_TOKEN = "NPT";

}
