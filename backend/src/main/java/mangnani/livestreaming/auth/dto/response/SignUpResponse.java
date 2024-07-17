package mangnani.livestreaming.auth.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class SignUpResponse extends ResponseDto {

	 private SignUpResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static SignUpResponse success() {
		return new SignUpResponse();
	}
}
