package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class ChangeNicknameResponse extends ResponseDto {

	private ChangeNicknameResponse () {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static ChangeNicknameResponse success() {
		return new ChangeNicknameResponse();
	}
}
