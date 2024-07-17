package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class ChangeProfileImageResponse extends ResponseDto {

	private ChangeProfileImageResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static ChangeProfileImageResponse success() {
		return new ChangeProfileImageResponse();
	}

}
