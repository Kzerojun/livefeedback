package mangnani.livestreaming.auth.dto.response;

import lombok.Getter;
import lombok.Setter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
@Setter
public class SignUpResponse extends ResponseDto {

	 private SignUpResponse() {
		super(ResponseCode.SIGN_UP_SUCCESS, ResponseMessage.SIGNUP_SUCCESS);
	}

	public static SignUpResponse success() {
		return new SignUpResponse();
	}
}
