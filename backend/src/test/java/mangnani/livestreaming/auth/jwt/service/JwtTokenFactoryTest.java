package mangnani.livestreaming.auth.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenFactoryTest {

	private JwtTokenFactory jwtTokenFactory;
	private final String testSecretKey = "testtest";
	private final long accessTokenExpireTime = 60000L; // 1 minute
	private final long refreshTokenExpireTime = 120000L; // 2 minutes

	@BeforeEach
	void setUp() {
		jwtTokenFactory = new JwtTokenFactory();
		ReflectionTestUtils.setField(jwtTokenFactory, "jwtSecret", testSecretKey);
		ReflectionTestUtils.setField(jwtTokenFactory, "accessTokenExpireTime", accessTokenExpireTime);
		ReflectionTestUtils.setField(jwtTokenFactory, "refreshTokenExpireTime", refreshTokenExpireTime);
	}

	@Test
	@DisplayName("AccessToken 생성 테스트")
	void testGenerateAccessToken() {
		// given
		List<SimpleGrantedAuthority> authorities = Arrays.asList(
				new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN"));
		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "password", authorities);

		// when
		String accessToken = jwtTokenFactory.generateAccessToken(authentication);

		// then
		assertNotNull(accessToken, "AccessToken이 생성되어야 합니다.");

		// 토큰의 claims 검증
		Claims claims = Jwts.parser()
				.setSigningKey(testSecretKey)
				.parseClaimsJws(accessToken)
				.getBody();

		assertEquals("testUser", claims.getSubject(), "토큰의 subject는 'testUser'여야 합니다.");
		assertTrue(claims.getExpiration().after(new Date()), "토큰의 만료 시간은 현재 시간 이후여야 합니다.");
		assertEquals("USER,ADMIN", claims.get("auth"), "토큰의 권한 정보가 일치해야 합니다.");
	}

	@Test
	@DisplayName("RefreshToken 생성 테스트")
	void testGenerateRefreshToken() {
		// when
		String refreshToken = jwtTokenFactory.generateRefreshToken();

		// then
		assertNotNull(refreshToken, "RefreshToken이 생성되어야 합니다.");

		// 토큰의 claims 검증
		Claims claims = Jwts.parser()
				.setSigningKey(testSecretKey)
				.parseClaimsJws(refreshToken)
				.getBody();

		assertTrue(claims.getExpiration().after(new Date()), "토큰의 만료 시간은 현재 시간 이후여야 합니다.");
	}
}
