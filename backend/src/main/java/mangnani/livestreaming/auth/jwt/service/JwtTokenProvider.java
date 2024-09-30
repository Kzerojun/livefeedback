package mangnani.livestreaming.auth.jwt.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.global.config.CustomUserDetailsService;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

	private final JwtTokenFactory tokenFactory;
	private final JwtTokenValidator tokenValidator;
	private final JwtTokenParser tokenParser;
	private final CustomUserDetailsService userDetailsService;
	private static final String BEARER_TYPE = "Bearer";

	public LoginResponse generateToken(Authentication authentication) {
		String accessToken = tokenFactory.generateAccessToken(authentication);
		String refreshToken = tokenFactory.generateRefreshToken();

		return LoginResponse.builder().grantType(BEARER_TYPE).accessToken(accessToken)
				.refreshToken(refreshToken)
				.accessTokenExpirationTime(tokenParser.getExpiration(accessToken))
				.refreshTokenExpirationTime(tokenParser.getExpiration(refreshToken)).build();
	}

	public boolean validate(String accessToken) {
		return tokenValidator.validate(accessToken);
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = tokenParser.parseClaims(accessToken);
		if (claims.get("auth") == null) {
			throw new NoPermissionTokenException();
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, accessToken,
				userDetails.getAuthorities());
	}
}

