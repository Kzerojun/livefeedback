package mangnani.livestreaming.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class LoginResponse extends ResponseDto {

	private final String grantType;

	private final String accessToken;

	private final String refreshToken;

	private final Long refreshTokenExpirationTIme;

	@Builder
	public LoginResponse(String grantType, String accessToken, String refreshToken,
			Long refreshTokenExpirationTIme) {
		super(ResponseCode.LOGIN_SUCCESS, ResponseMessage.SIGNUP_SUCCESS);
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.refreshTokenExpirationTIme = refreshTokenExpirationTIme;
	}
}
