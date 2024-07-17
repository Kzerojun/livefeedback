package mangnani.livestreaming.auth.service.implement;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.auth.constatnts.AuthConstants;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.LogoutResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import mangnani.livestreaming.auth.exception.DuplicatedLoginIdException;
import mangnani.livestreaming.auth.exception.DuplicatedNicknameException;
import mangnani.livestreaming.auth.service.AuthService;
import mangnani.livestreaming.auth.util.StreamKeyGenerator;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import mangnani.livestreaming.auth.jwt.provider.JwtTokenProvider;
import mangnani.livestreaming.auth.jwt.service.TokenBlackListService;
import mangnani.livestreaming.member.constant.Role;
import mangnani.livestreaming.member.constant.Status;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
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
@Slf4j
public class AuthServiceImpl implements AuthService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, String> redisTemplate;
	private final TokenBlackListService tokenBlackListService;

	@Override
	public ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest) {
		//고유 아이디 인지 검증
		if (memberRepository.existsByLoginId(signUpRequest.getLoginId())) {
			throw new DuplicatedLoginIdException();
		}

		//고유 닉네임인지 검증
		if (memberRepository.existsByNickname(signUpRequest.getNickname())) {
			throw new DuplicatedNicknameException();
		}

		String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

		Member user = Member.builder()
				.loginId(signUpRequest.getLoginId())
				.password(encodedPassword)
				.nickname(signUpRequest.getNickname())
				.streamKey(StreamKeyGenerator.generateStreamKey())
				.role(Role.USER)
				.status(Status.ACTIVATE)
				.build();

		memberRepository.save(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(SignUpResponse.success());
	}


	@Override
	public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {

		if (!memberRepository.existsByLoginId(loginRequest.getLoginId())) {
			throw new NoExistedMember();
		}

		UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
		Authentication authentication = authenticationManagerBuilder.getObject()
				.authenticate(authenticationToken);

		LoginResponse loginResponse = jwtTokenProvider.generateToken(authentication);


		redisTemplate.opsForValue().set(AuthConstants.REFRESH_TOKEN_PREFIX + authentication.getName(),
				loginResponse.getRefreshToken(),
				loginResponse.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

		return ResponseEntity.ok().body(loginResponse);
	}

	@Override
	public ResponseEntity<String> reissue(String accessToken, String refreshToken) {
		if (!jwtTokenProvider.validate(refreshToken)) {
			throw new NoPermissionTokenException();
		}

		Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

		String redisRefreshToken = redisTemplate.opsForValue()
				.get(AuthConstants.REFRESH_TOKEN_PREFIX + authentication.getName());

		if (!refreshToken.equals(redisRefreshToken)) {
			throw new NoPermissionTokenException();
		}

		return ResponseEntity.ok().body(jwtTokenProvider.generateAccessToken(authentication));
	}

	@Override
	public ResponseEntity<LogoutResponse> logout(String userLoginId, String accessToken) {
		String refreshTokenKey = AuthConstants.REFRESH_TOKEN_PREFIX + userLoginId;
		String refreshToken = redisTemplate.opsForValue().get(refreshTokenKey);

		if (refreshToken != null) {
			redisTemplate.delete(refreshTokenKey);
		}

		Long expiration = jwtTokenProvider.getExpiration(accessToken);
		tokenBlackListService.blacklistToken(accessToken,expiration);
		return ResponseEntity.ok().body(LogoutResponse.success());
	}
}
