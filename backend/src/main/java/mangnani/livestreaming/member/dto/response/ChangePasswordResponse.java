package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class ChangePasswordResponse extends ResponseDto {

	private ChangePasswordResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static ChangePasswordResponse success() {
		return new ChangePasswordResponse();
	}
}
