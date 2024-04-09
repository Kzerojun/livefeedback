package mangnani.livestreaming.auth.service.implement;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.auth.dto.request.LogoutRequest;
import mangnani.livestreaming.auth.dto.request.ReissueRequest;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import mangnani.livestreaming.auth.exception.DuplicatedLoginIdException;
import mangnani.livestreaming.auth.exception.DuplicatedNicknameException;
import mangnani.livestreaming.auth.exception.LoginFailedException;
import mangnani.livestreaming.auth.service.AuthService;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import mangnani.livestreaming.global.jwt.provider.JwtTokenProvider;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final JwtTokenProvider jwtTokenProvider;

	private final RedisTemplate<String, Object> redisTemplate;

	private static final String REFRESH_TOKEN_PREFIX = "RT:";

	private static final String LOGOUT_STATUS_VALUE = "logout";
	@Override
	public ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest) {
		if (memberRepository.existsByLoginId(signUpRequest.getLoginId())) {
			throw new DuplicatedLoginIdException();
		}

		if (memberRepository.existsByNickname(signUpRequest.getNickname())) {
			throw new DuplicatedNicknameException();
		}

		String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

		Member user = Member.builder()
				.loginId(signUpRequest.getLoginId())
				.password(encodedPassword)
				.nickname(signUpRequest.getNickname())
				.build();

		memberRepository.save(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(SignUpResponse.success());
	}

	@Override
	public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {

		if (!memberRepository.existsByLoginId(loginRequest.getLoginId())) {
			throw new LoginFailedException();
		}

		UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
		Authentication authentication = authenticationManagerBuilder.getObject()
				.authenticate(authenticationToken);

		LoginResponse loginResponse = jwtTokenProvider.generateToken(authentication);
		redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + authentication.getName(),
				loginResponse.getRefreshToken(),
				loginResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

		return ResponseEntity.ok().body(loginResponse);
	}

	@Override
	public ResponseEntity<?> reissue(ReissueRequest reissue) {
		if (!jwtTokenProvider.validate(reissue.getRefreshToken())) {
			throw new NoPermissionTokenException();
		}

		Authentication authentication = jwtTokenProvider.getAuthentication(
				reissue.getAccessToken());

		String refreshToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + authentication.getName());
		if (!refreshToken.equals(reissue.getRefreshToken())) {
			throw new NoPermissionTokenException();
		}

		LoginResponse loginResponse = jwtTokenProvider.generateToken(authentication);
		redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + authentication.getName(),
				loginResponse.getRefreshToken(),
				loginResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> logout(LogoutRequest logoutRequest) {

		if (!jwtTokenProvider.validate(logoutRequest.getAccessToken())) {
			return ResponseEntity.badRequest().build();
		}

		Authentication authentication = jwtTokenProvider.getAuthentication(
				logoutRequest.getAccessToken());

		//Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지
		if (redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + authentication.getName()) != null) {
			// Refresh Token 삭제
			redisTemplate.delete(REFRESH_TOKEN_PREFIX + authentication.getName());
		}

		Long expiration = jwtTokenProvider.getExpiration(logoutRequest.getAccessToken());
		redisTemplate.opsForValue()
				.set(logoutRequest.getRefreshToken(), LOGOUT_STATUS_VALUE, expiration, TimeUnit.MILLISECONDS);

		return ResponseEntity.ok().build();
	}
}
