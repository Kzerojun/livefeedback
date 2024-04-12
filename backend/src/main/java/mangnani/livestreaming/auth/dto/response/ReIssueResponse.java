package mangnani.livestreaming.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class ReIssueResponse extends ResponseDto {

	private final String grantType;
	private final String accessToken;
	private final Long accessTokenExpirationTime;

	@Builder
	public ReIssueResponse(String grantType, String accessToken,Long accessTokenExpirationTime){
		super(ResponseCode.REISSUE_TOKEN_SUCCESS, ResponseMessage.REISSUE_TOKEN_SUCCESS);
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.accessTokenExpirationTime = accessTokenExpirationTime;
	}
}
