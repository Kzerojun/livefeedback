package mangnani.livestreaming.auth.jwt.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenValidatorTest {

	private JwtTokenValidator jwtTokenValidator;
	private final String testSecretKey = "testtest";

	@BeforeEach
	void setUp() {
		jwtTokenValidator = new JwtTokenValidator();
		ReflectionTestUtils.setField(jwtTokenValidator, "jwtSecret", testSecretKey);
	}

	@Test
	@DisplayName("유효한 토큰을 검증합니다.") // 한글 설명
	void validateValidToken() { // 영어 메소드 명
		// given
		String validToken = Jwts.builder()
				.setSubject("test")
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, testSecretKey)
				.compact();

		// when
		boolean isValid = jwtTokenValidator.validate(validToken);

		// then
		assertTrue(isValid, "유효한 토큰이어야 합니다.");
	}

	@Test
	@DisplayName("만료된 토큰을 검증합니다.") // 한글 설명
	void validateExpiredToken() { // 영어 메소드 명
		// given
		String expiredToken = Jwts.builder()
				.setSubject("test")
				.setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 10)) // 10분 전에 만료
				.signWith(SignatureAlgorithm.HS256, testSecretKey)
				.compact();

		// when
		boolean isValid = jwtTokenValidator.validate(expiredToken);

		// then
		assertFalse(isValid, "만료된 토큰이어야 합니다.");
	}

	@Test
	@DisplayName("잘못된 서명을 가진 토큰을 검증합니다.") // 한글 설명
	void validateTokenWithWrongSignature() { // 영어 메소드 명
		// given
		String validTokenWithWrongKey = Jwts.builder()
				.setSubject("test")
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, "test123") // 다른 서명 키 사용
				.compact();

		// when
		boolean isValid = jwtTokenValidator.validate(validTokenWithWrongKey);

		// then
		assertFalse(isValid, "잘못된 서명의 토큰이어야 합니다.");
	}
}
