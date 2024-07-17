package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class CheckNicknameAvailabilityResponse extends ResponseDto {

	private CheckNicknameAvailabilityResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static CheckNicknameAvailabilityResponse success() {
		return new CheckNicknameAvailabilityResponse();
	}
}
