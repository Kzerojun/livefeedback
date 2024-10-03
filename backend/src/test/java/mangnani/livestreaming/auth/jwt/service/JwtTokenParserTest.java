package mangnani.livestreaming.auth.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import mangnani.livestreaming.global.config.CustomUserDetailsService;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import mangnani.livestreaming.member.constant.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenParserTest {

	private JwtTokenParser jwtTokenParser;
	private final String testSecretKey = "testtest";
	private final CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);

	@BeforeEach
	void setUp() {
		jwtTokenParser = new JwtTokenParser(userDetailsService);
		ReflectionTestUtils.setField(jwtTokenParser, "jwtSecret", testSecretKey);
	}

	@Test
	@DisplayName("parseClaims 성공 테스트")
	void testParseClaims() {
		// given
		String validToken = createToken("testUser", Role.USER.name());

		// when
		Claims claims = jwtTokenParser.parseClaims(validToken);

		// then
		assertAll(
				() -> assertNotNull(claims, "Claims가 null이면 안됩니다."),
				() -> assertEquals("testUser", claims.getSubject(), "주체(subject)는 'testUser'여야 합니다."),
				() -> assertEquals(Role.USER.name(), claims.get("auth"), "권한 정보는 'USER'여야 합니다.")
		);
	}

	@Test
	@DisplayName("parseClaims 만료된 토큰 테스트")
	void testParseExpiredClaims() {
		// given
		String expiredToken = Jwts.builder()
				.setSubject("testUser")
				.setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // 1 minute past expiration
				.claim("auth", Role.USER.name())
				.signWith(SignatureAlgorithm.HS256, testSecretKey)
				.compact();

		// when
		Claims claims = jwtTokenParser.parseClaims(expiredToken);

		// then
		assertAll(
				() -> assertNotNull(claims, "만료된 토큰의 Claims도 반환되어야 합니다."),
				() -> assertEquals("testUser", claims.getSubject(), "주체(subject)는 'testUser'여야 합니다.")
		);
	}

	@Test
	@DisplayName("getExpiration 테스트")
	void testGetExpiration() {
		// given
		String token = createToken("testUser", Role.USER.name());

		// when
		Long expiration = jwtTokenParser.getExpiration(token);

		// then
		assertTrue(expiration > 0, "토큰의 만료 시간은 현재 시간보다 나중이어야 합니다.");
	}

	@Test
	@DisplayName("getAuthentication 성공 테스트")
	void testGetAuthenticationSuccess() {
		// given
		String testUsername = "testUser";
		String validToken = createToken(testUsername, Role.USER.name());

		UserDetails userDetails = User.builder()
				.username(testUsername)
				.password("password")
				.authorities(Role.USER.name())
				.build();

		when(userDetailsService.loadUserByUsername(testUsername)).thenReturn(userDetails);

		// when
		Authentication authentication = jwtTokenParser.getAuthentication(validToken);

		// then
		assertAll(
				() -> assertNotNull(authentication, "Authentication 객체가 null이면 안됩니다."),
				() -> assertEquals(testUsername, authentication.getName(), "Authentication의 이름은 일치해야 합니다."),
				() -> assertTrue(authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals(Role.USER.name())), "권한이 일치해야 합니다."),
				() -> assertEquals(validToken, authentication.getCredentials(), "Credentials는 토큰과 일치해야 합니다."),
				() -> assertEquals(userDetails, authentication.getPrincipal(), "Principal은 UserDetails와 일치해야 합니다.")
		);

		verify(userDetailsService).loadUserByUsername(testUsername);
	}

	@Test
	@DisplayName("getAuthentication 권한이 없는 토큰 실패 테스트")
	void testGetAuthenticationNoAuth() {
		// given
		String tokenWithoutAuth = Jwts.builder()
				.setSubject("testUser")
				.setExpiration(new Date(System.currentTimeMillis() + 60000))
				.signWith(SignatureAlgorithm.HS256, testSecretKey)
				.compact();

		// when & then
		assertThrows(NoPermissionTokenException.class,
				() -> jwtTokenParser.getAuthentication(tokenWithoutAuth),
				"권한이 없는 토큰은 NoPermissionTokenException을 발생시켜야 합니다."
		);
	}

	private String createToken(String username, String role) {
		return Jwts.builder()
				.setSubject(username)
				.claim("auth", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SignatureAlgorithm.HS256, testSecretKey)
				.compact();
	}
}