package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetSignInMemberResponseDto extends ResponseDto {

	private String userId;
	private String nickname;
	private String streamKey;

	private GetSignInMemberResponseDto(String userId, String nickname,String streamKey) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.userId = userId;
		this.nickname = nickname;
		this.streamKey = streamKey;
	}

	public static GetSignInMemberResponseDto success(String loginId, String nickname,String streamKey) {
		return new GetSignInMemberResponseDto(loginId, nickname,streamKey);
	}

}
