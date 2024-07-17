package mangnani.livestreaming.member.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetStarCandyAmountResponse extends ResponseDto {

	private final String nickname;

	private final int starCandyAmount;

	private GetStarCandyAmountResponse(String nickname, int starCandyAmount) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.nickname = nickname;
		this.starCandyAmount = starCandyAmount;
	}

	public static GetStarCandyAmountResponse success(String nickname, int starCandyAmount) {
		return new GetStarCandyAmountResponse(nickname, starCandyAmount);
	}
}
