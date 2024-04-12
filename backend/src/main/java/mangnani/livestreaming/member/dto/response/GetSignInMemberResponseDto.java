package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetSignInMemberResponseDto extends ResponseDto {

	private String loginId;
	private String nickname;

	private GetSignInMemberResponseDto(String loginId, String nickname) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.loginId = loginId;
		this.nickname = nickname;
	}

	public static GetSignInMemberResponseDto success(String loginId, String nickname) {
		return new GetSignInMemberResponseDto(loginId, nickname);
	}
}
