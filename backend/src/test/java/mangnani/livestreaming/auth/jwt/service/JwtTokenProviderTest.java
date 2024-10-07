package mangnani.livestreaming.auth.jwt.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Claims;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.global.config.CustomUserDetailsService;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWT 토큰 제공자 테스트")
class JwtTokenProviderTest {

	@Mock
	private JwtTokenFactory tokenFactory;
	@Mock
	private JwtTokenValidator tokenValidator;
	@Mock
	private JwtTokenParser tokenParser;
	@Mock
	private CustomUserDetailsService userDetailsService;
	@Mock
	private Authentication authentication;

	@InjectMocks
	private JwtTokenProvider jwtTokenProvider;

	private static final String MOCK_ACCESS_TOKEN = "mockAccessToken";
	private static final String MOCK_REFRESH_TOKEN = "mockRefreshToken";

	@Nested
	@DisplayName("토큰 생성 메소드")
	class GenerateTokenTest {

		@Test
		@DisplayName("토큰이 성공적으로 생성되어야 함")
		void shouldGenerateTokensSuccessfully() {
			// Arrange
			when(tokenFactory.generateAccessToken(authentication)).thenReturn(MOCK_ACCESS_TOKEN);
			when(tokenFactory.generateRefreshToken()).thenReturn(MOCK_REFRESH_TOKEN);
			when(tokenParser.getExpiration(MOCK_ACCESS_TOKEN)).thenReturn(3600000L);
			when(tokenParser.getExpiration(MOCK_REFRESH_TOKEN)).thenReturn(86400000L);

			// Act
			LoginResponse response = jwtTokenProvider.generateToken(authentication);

			// Assert
			assertNotNull(response);
			assertEquals("Bearer", response.getGrantType());
			assertEquals(MOCK_ACCESS_TOKEN, response.getAccessToken());
			assertEquals(MOCK_REFRESH_TOKEN, response.getRefreshToken());
			assertEquals(3600000L, response.getAccessTokenExpirationTime());
			assertEquals(86400000L, response.getRefreshTokenExpirationTime());

			verify(tokenFactory).generateAccessToken(authentication);
			verify(tokenFactory).generateRefreshToken();
			verify(tokenParser).getExpiration(MOCK_ACCESS_TOKEN);
			verify(tokenParser).getExpiration(MOCK_REFRESH_TOKEN);
		}
	}

	@Nested
	@DisplayName("토큰 검증 메소드")
	class ValidateTest {

		@Test
		@DisplayName("유효한 토큰에 대해 true를 반환해야 함")
		void shouldReturnTrueForValidToken() {
			// Arrange
			when(tokenValidator.validate(MOCK_ACCESS_TOKEN)).thenReturn(true);

			// Act
			boolean result = jwtTokenProvider.validate(MOCK_ACCESS_TOKEN);

			// Assert
			assertTrue(result);
			verify(tokenValidator).validate(MOCK_ACCESS_TOKEN);
		}

		@Test
		@DisplayName("유효하지 않은 토큰에 대해 false를 반환해야 함")
		void shouldReturnFalseForInvalidToken() {
			// Arrange
			when(tokenValidator.validate(MOCK_ACCESS_TOKEN)).thenReturn(false);

			// Act
			boolean result = jwtTokenProvider.validate(MOCK_ACCESS_TOKEN);

			// Assert
			assertFalse(result);
			verify(tokenValidator).validate(MOCK_ACCESS_TOKEN);
		}
	}

	@Nested
	@DisplayName("인증 정보 추출 메소드")
	class GetAuthenticationTest {

		@Mock
		private Claims claims;
		@Mock
		private UserDetails userDetails;

		@Test
		@DisplayName("유효한 토큰에 대해 인증 정보를 반환해야 함")
		void shouldReturnAuthenticationForValidToken() {
			// Arrange
			when(tokenParser.parseClaims(MOCK_ACCESS_TOKEN)).thenReturn(claims);
			when(claims.getSubject()).thenReturn("testUser");
			when(claims.get("auth")).thenReturn("ROLE_USER");
			when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

			// Act
			Authentication result = jwtTokenProvider.getAuthentication(MOCK_ACCESS_TOKEN);

			// Assert
			assertNotNull(result);
			assertEquals(userDetails, result.getPrincipal());
			assertEquals(MOCK_ACCESS_TOKEN, result.getCredentials());

			verify(tokenParser).parseClaims(MOCK_ACCESS_TOKEN);
			verify(userDetailsService).loadUserByUsername("testUser");
		}

		@Test
		@DisplayName("권한 정보가 없을 때 NoPermissionTokenException을 던져야 함")
		void shouldThrowExceptionWhenAuthClaimIsMissing() {
			// Arrange
			when(tokenParser.parseClaims(MOCK_ACCESS_TOKEN)).thenReturn(claims);
			when(claims.get("auth")).thenReturn(null);

			// Act & Assert
			assertThrows(NoPermissionTokenException.class, () ->
					jwtTokenProvider.getAuthentication(MOCK_ACCESS_TOKEN)
			);

			verify(tokenParser).parseClaims(MOCK_ACCESS_TOKEN);
			verifyNoInteractions(userDetailsService);
		}
	}
}