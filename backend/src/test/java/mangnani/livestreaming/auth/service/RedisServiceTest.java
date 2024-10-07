package mangnani.livestreaming.auth.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
@DisplayName("Redis 서비스 테스트")
class RedisServiceTest {

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	private RedisService redisService;

	@BeforeEach
	void setUp() {
		redisService = new RedisService(redisTemplate);
	}

	@Test
	@DisplayName("리프레시 토큰 저장 테스트")
	void testSaveRefreshToken() {
		// Arrange
		String key = "user:refreshToken";
		String token = "refreshTokenValue";
		long expirationTime = 3600000; // 1 hour in milliseconds

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);

		// Act
		redisService.saveRefreshToken(key, token, expirationTime);

		// Assert
		verify(valueOperations).set(key, token, expirationTime, TimeUnit.MILLISECONDS);
	}

	@Test
	@DisplayName("리프레시 토큰 조회 테스트")
	void testGetRefreshToken() {
		// Arrange
		String key = "user:refreshToken";
		String expectedToken = "refreshTokenValue";
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(expectedToken);

		// Act
		String actualToken = redisService.getRefreshToken(key);

		// Assert
		assertThat(actualToken).isEqualTo(expectedToken);
		verify(valueOperations).get(key);
	}

	@Test
	@DisplayName("리프레시 토큰 삭제 테스트")
	void testDeleteRefreshToken() {
		// Arrange
		String key = "user:refreshToken";

		// Act
		redisService.deleteRefreshToken(key);

		// Assert
		verify(redisTemplate).delete(key);
	}
}