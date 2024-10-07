package mangnani.livestreaming.auth.service;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.jwt.service.JwtTokenFactory;
import mangnani.livestreaming.auth.jwt.service.JwtTokenParser;
import mangnani.livestreaming.auth.jwt.service.JwtTokenProvider;
import mangnani.livestreaming.auth.jwt.service.JwtTokenValidator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceSupport {

	private final JwtTokenFactory jwtTokenFactory;
	private final JwtTokenValidator jwtTokenValidator;
	private final JwtTokenParser jwtTokenParser;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	// 로그인 요청을 통한 인증 수행
	public Authentication authenticate(LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
		return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
	}

	// 액세스 토큰 생성
	public String generateAccessToken(Authentication authentication) {
		return jwtTokenFactory.generateAccessToken(authentication);
	}

	// 액세스 및 리프레시 토큰 생성
	public LoginResponse generateToken(Authentication authentication) {
		return jwtTokenProvider.generateToken(authentication);
	}

	// 토큰 유효성 검사
	public boolean validateToken(String token) {
		return jwtTokenValidator.validate(token);
	}

	// 토큰에서 인증 정보 추출
	public Authentication getAuthentication(String token) {
		return jwtTokenParser.getAuthentication(token);
	}

	// 토큰 만료 시간 가져오기
	public Long getExpiration(String accessToken) {
		return jwtTokenParser.getExpiration(accessToken);
	}
}
