package mangnani.livestreaming.auth.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.LogoutResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import mangnani.livestreaming.auth.exception.DuplicatedLoginIdException;
import mangnani.livestreaming.auth.exception.DuplicatedNicknameException;
import mangnani.livestreaming.auth.jwt.service.TokenBlackListService;
import mangnani.livestreaming.auth.service.AuthServiceImpl;
import mangnani.livestreaming.auth.service.AuthServiceSupport;
import mangnani.livestreaming.auth.service.RedisService;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 서비스 구현체 테스트")
class AuthServiceImplTest {

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthServiceSupport authServiceSupport;
	@Mock
	private RedisService redisService;
	@Mock
	private TokenBlackListService tokenBlackListService;

	@InjectMocks
	private AuthServiceImpl authService;

	@Nested
	@DisplayName("회원가입 테스트")
	class SignUpTest {
		private SignUpRequest signUpRequest;

		@BeforeEach
		void setUp() {
			signUpRequest = new SignUpRequest("testUser", "password", "nickname");
		}

		@Test
		@DisplayName("정상적인 회원가입 요청 처리")
		void testSignUpSuccess() {
			when(memberRepository.existsByLoginId(anyString())).thenReturn(false);
			when(memberRepository.existsByNickname(anyString())).thenReturn(false);
			when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

			ResponseEntity<SignUpResponse> response = authService.signUp(signUpRequest);

			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			assertEquals("SU", Objects.requireNonNull(response.getBody()).getCode());
			verify(memberRepository).save(any(Member.class));
		}

		@Test
		@DisplayName("중복된 로그인 ID로 회원가입 시도")
		void testSignUpDuplicateLoginId() {
			when(memberRepository.existsByLoginId(anyString())).thenReturn(true);

			assertThrows(DuplicatedLoginIdException.class, () -> authService.signUp(signUpRequest));
		}

		@Test
		@DisplayName("중복된 닉네임으로 회원가입 시도")
		void testSignUpDuplicateNickname() {
			when(memberRepository.existsByLoginId(anyString())).thenReturn(false);
			when(memberRepository.existsByNickname(anyString())).thenReturn(true);

			assertThrows(DuplicatedNicknameException.class, () -> authService.signUp(signUpRequest));
		}
	}

	@Nested
	@DisplayName("로그인 테스트")
	class LoginTest {
		private LoginRequest loginRequest;
		private Authentication authentication;

		@BeforeEach
		void setUp() {
			loginRequest = new LoginRequest("testUser", "password");
			authentication = mock(Authentication.class);
		}

		@Test
		@DisplayName("정상적인 로그인 요청 처리")
		void testLoginSuccess() {
			when(memberRepository.existsByLoginId(anyString())).thenReturn(true);
			when(authServiceSupport.authenticate(any(LoginRequest.class))).thenReturn(authentication);

			// LoginResponse 객체를 빌더 패턴을 사용하여 생성
			LoginResponse mockLoginResponse = LoginResponse.builder()
					.grantType("Bearer")
					.accessToken("accessToken")
					.refreshToken("refreshToken")
					.accessTokenExpirationTime(3600L)
					.refreshTokenExpirationTime(86400L)
					.build();

			when(authServiceSupport.generateToken(any(Authentication.class))).thenReturn(mockLoginResponse);
			when(authentication.getName()).thenReturn("testUser");

			ResponseEntity<LoginResponse> response = authService.login(loginRequest);

			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertNotNull(response.getBody());
			assertEquals("Bearer", response.getBody().getGrantType());
			assertEquals("accessToken", response.getBody().getAccessToken());
			assertEquals("refreshToken", response.getBody().getRefreshToken());
			assertEquals(3600L, response.getBody().getAccessTokenExpirationTime());
			assertEquals(86400L, response.getBody().getRefreshTokenExpirationTime());
			verify(redisService).saveRefreshToken(anyString(), anyString(), anyLong());
		}

		@Test
		@DisplayName("존재하지 않는 사용자로 로그인 시도")
		void testLoginNonExistentUser() {
			when(memberRepository.existsByLoginId(anyString())).thenReturn(false);

			assertThrows(NoExistedMember.class, () -> authService.login(loginRequest));
		}
	}

	@Nested
	@DisplayName("토큰 재발급 테스트")
	class ReissueTest {
		@Test
		@DisplayName("정상적인 토큰 재발급 요청 처리")
		void testReissueSuccess() {
			String accessToken = "oldAccessToken";
			String refreshToken = "validRefreshToken";
			Authentication authentication = mock(Authentication.class);

			when(authServiceSupport.validateToken(refreshToken)).thenReturn(true);
			when(authServiceSupport.getAuthentication(accessToken)).thenReturn(authentication);
			when(authentication.getName()).thenReturn("testUser");
			when(redisService.getRefreshToken(anyString())).thenReturn(refreshToken);
			when(authServiceSupport.generateAccessToken(authentication)).thenReturn("newAccessToken");

			ResponseEntity<String> response = authService.reissue(accessToken, refreshToken);

			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals("newAccessToken", response.getBody());
		}

		@Test
		@DisplayName("유효하지 않은 리프레시 토큰으로 재발급 시도")
		void testReissueInvalidRefreshToken() {
			when(authServiceSupport.validateToken(anyString())).thenReturn(false);

			assertThrows(NoPermissionTokenException.class, () -> authService.reissue("accessToken", "invalidRefreshToken"));
		}
	}

	@Nested
	@DisplayName("로그아웃 테스트")
	class LogoutTest {
		@Test
		@DisplayName("정상적인 로그아웃 요청 처리")
		void testLogoutSuccess() {
			String userLoginId = "testUser";
			String accessToken = "validAccessToken";

			when(redisService.getRefreshToken(anyString())).thenReturn("refreshToken");
			when(authServiceSupport.getExpiration(accessToken)).thenReturn(3600L);

			ResponseEntity<LogoutResponse> response = authService.logout(userLoginId, accessToken);

			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("성공");
			verify(redisService).deleteRefreshToken(anyString());
			verify(tokenBlackListService).blacklistToken(accessToken, 3600L);
		}
	}
}