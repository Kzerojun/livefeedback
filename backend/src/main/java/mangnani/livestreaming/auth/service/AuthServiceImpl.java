package mangnani.livestreaming.auth.service;

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
import mangnani.livestreaming.auth.util.StreamKeyGenerator;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import mangnani.livestreaming.auth.jwt.service.TokenBlackListService;
import mangnani.livestreaming.member.constant.Role;
import mangnani.livestreaming.member.constant.Status;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthServiceSupport authServiceSupport;
	private final RedisService redisService;
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

		Authentication authentication = authServiceSupport.authenticate(loginRequest);
		LoginResponse loginResponse = authServiceSupport.generateToken(authentication);

		// Redis 작업을 RedisService로 위임
		redisService.saveRefreshToken(AuthConstants.REFRESH_TOKEN_PREFIX + authentication.getName(),
				loginResponse.getRefreshToken(),
				loginResponse.getRefreshTokenExpirationTime());

		return ResponseEntity.ok().body(loginResponse);
	}

	@Override
	public ResponseEntity<String> reissue(String accessToken, String refreshToken) {
		if (!authServiceSupport.validateToken(refreshToken)) {
			throw new NoPermissionTokenException();
		}

		Authentication authentication = authServiceSupport.getAuthentication(accessToken);
		String redisRefreshToken = redisService.getRefreshToken(AuthConstants.REFRESH_TOKEN_PREFIX + authentication.getName());

		if (!refreshToken.equals(redisRefreshToken)) {
			throw new NoPermissionTokenException();
		}

		return ResponseEntity.ok().body(authServiceSupport.generateAccessToken(authentication));
	}

	@Override
	public ResponseEntity<LogoutResponse> logout(String userLoginId, String accessToken) {
		String refreshTokenKey = AuthConstants.REFRESH_TOKEN_PREFIX + userLoginId;
		String refreshToken = redisService.getRefreshToken(refreshTokenKey);

		if (refreshToken != null) {
			redisService.deleteRefreshToken(refreshTokenKey);
		}

		Long expiration = authServiceSupport.getExpiration(accessToken);
		tokenBlackListService.blacklistToken(accessToken, expiration);

		return ResponseEntity.ok().body(LogoutResponse.success());
	}
}
