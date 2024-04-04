package mangnani.livestreaming.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignInResponse {

	private String grantType;

	private String accessToken;

	private String refreshToken;

	private Long refreshTokenExpirationTIme;

}
