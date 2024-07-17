package mangnani.livestreaming.auth.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import mangnani.livestreaming.auth.exception.DuplicatedLoginIdException;
import mangnani.livestreaming.auth.exception.DuplicatedNicknameException;
import mangnani.livestreaming.auth.exception.LoginFailedException;
import mangnani.livestreaming.auth.jwt.provider.JwtTokenProvider;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@InjectMocks
	private AuthServiceImpl authService;


	@DisplayName("회원가입 성공")
	@Test
	void signUp_Success() {
		SignUpRequest signUpRequest = signUpRequest();

		when(memberRepository.existsByLoginId(anyString())).thenReturn(false);
		when(memberRepository.existsByNickname(anyString())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		ResponseEntity<SignUpResponse> response = authService.signUp(signUpRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getCode()).isEqualTo("SU");
		assertThat(response.getBody().getMessage()).isEqualTo("성공");
		verify(memberRepository, times(1)).save(any());
	}

	@DisplayName("중복된 이메일 회원가입 실패")
	@Test
	void signUp_Fail_Duplicated_Email() {
		SignUpRequest signUpRequest = signUpRequest();
		when(memberRepository.existsByLoginId(anyString())).thenReturn(true);

		assertThatThrownBy(() -> authService.signUp(signUpRequest)).isInstanceOf(
				DuplicatedLoginIdException.class);

		DuplicatedLoginIdException response = assertThrows(
				DuplicatedLoginIdException.class, () -> authService.signUp(signUpRequest));

		assertThat(response.getCode()).isEqualTo("DLI");
		assertThat(response.getMessage()).isEqualTo("중복된 아이디 입니다.");
	}

	@DisplayName("중복된 닉네임 회원가입 실패")
	@Test
	void signUp_Fail_Duplicated_Nickname() {
		SignUpRequest signUpRequest = signUpRequest();
		when(memberRepository.existsByNickname(anyString())).thenReturn(true);

		assertThatThrownBy(() -> authService.signUp(signUpRequest)).isInstanceOf(
				DuplicatedNicknameException.class);

		DuplicatedNicknameException response = assertThrows(
				DuplicatedNicknameException.class, () -> authService.signUp(signUpRequest));

		assertThat(response.getCode()).isEqualTo("DN");
		assertThat(response.getMessage()).isEqualTo("중복된 닉네임 입니다.");
	}


	@DisplayName("로그인 성공")
	@Test
	void login_SUCCESS() {
		LoginRequest loginRequest = loginRequest();
		Authentication authentication = mock(Authentication.class);
		LoginResponse loginResponse = loginResponse();

		when(memberRepository.existsByLoginId(loginRequest.getLoginId())).thenReturn(true);
		when(authenticationManagerBuilder.getObject()).thenReturn(mock(AuthenticationManager.class));
		when(authenticationManagerBuilder.getObject().authenticate(any(
				UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
		when(jwtTokenProvider.generateToken(authentication)).thenReturn(loginResponse);
		when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
		ResponseEntity<LoginResponse> response = authService.login(loginRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMessage()).isEqualTo("성공");
		assertThat(response.getBody().getCode()).isEqualTo("SU");
		assertThat(response.getBody().getAccessToken()).isEqualTo("accessToken");
		assertThat(response.getBody().getRefreshToken()).isEqualTo("refreshToken");
		assertThat(response.getBody().getGrantType()).isEqualTo("Bearer");
		assertThat(response.getBody().getRefreshTokenExpirationTime()).isEqualTo(100L);
	}

	@DisplayName("회원존재 X 로그인 실패")
	@Test
	void login_Failed() {
		when(memberRepository.existsByLoginId(loginRequest().getLoginId())).thenReturn(false);

		assertThatThrownBy(() -> authService.login(loginRequest())).isInstanceOf(
				LoginFailedException.class);
	}

	@DisplayName("로그아웃 성공")
	@Test
	void Logout_Success() {
		//given


	}

	private SignUpRequest signUpRequest() {
		return new SignUpRequest("test@example.com", "password", "testUser");
	}

	private LoginRequest loginRequest() {
		return new LoginRequest("test@exmaple.com", "password");
	}

	private LoginResponse loginResponse() {
		return LoginResponse.builder()
				.accessToken("accessToken")
				.refreshToken("refreshToken")
				.grantType("Bearer")
				.refreshTokenExpirationTime(100L)
				.build();
	}
}