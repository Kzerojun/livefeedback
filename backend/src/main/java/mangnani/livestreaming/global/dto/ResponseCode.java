package mangnani.livestreaming.global.dto;

public interface ResponseCode {

	//HTTP STATUS 200
	String LOGIN_SUCCESS = "LS";
	String DONATION_SUCCESS = "DS";
	String PATCH_IMAGE_SUCCESS = "PIS";
	String Patch_Description_SUCCESS = "PDS";

	String REISSUE_TOKEN_SUCCESS = "RTS";

	//HTTP STATUS 201
	String SIGN_UP_SUCCESS = "SUS";

	//HTTP STATUS 400
	String DUPLICATED_LOGIN_ID = "DLI";
	String DUPLICATED_NICKNAME = "DN";

	String NO_EXISTED_MEMBER = "NEM";

	String VALIDATION_FAILED = "VF";

	String NO_EXISTED_STATION = "NES";

	//HTTP STATUS 401
	String LOGIN_FAILED = "LF";

	String NO_PERMISSION_TOKEN = "NPT";

	//HTTP STATUS 500
	String DATABASE_ERROR = "DBE";

}
