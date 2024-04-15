package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetMemberResponse extends ResponseDto {

	private final String userId;
	private final String nickname;
	private final String profileImage;

	private GetMemberResponse(String userId, String nickname, String profileImage) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.userId = userId;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

	public static GetMemberResponse success(String userId, String nickname, String profileImage) {
		return new GetMemberResponse(userId, nickname, profileImage);
	}
}
