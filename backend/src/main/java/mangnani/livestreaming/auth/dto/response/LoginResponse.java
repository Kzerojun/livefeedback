package mangnani.livestreaming.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class LoginResponse extends ResponseDto {

	private final String grantType;

	private final String accessToken;

	private final String refreshToken;

	private final Long accessTokenExpirationTime;

	private final Long refreshTokenExpirationTime;

	@Builder
	private LoginResponse(String grantType, String accessToken, String refreshToken, Long accessTokenExpirationTime,
			Long refreshTokenExpirationTime) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpirationTime = accessTokenExpirationTime;
		this.refreshTokenExpirationTime = refreshTokenExpirationTime;
	}
}
