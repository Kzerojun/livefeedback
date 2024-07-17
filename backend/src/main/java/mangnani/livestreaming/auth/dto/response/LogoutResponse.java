package mangnani.livestreaming.auth.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class LogoutResponse extends ResponseDto {

	private LogoutResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static LogoutResponse success() {
		return new LogoutResponse();
	}
}
