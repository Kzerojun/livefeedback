package mangnani.livestreaming.auth.service;

import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;

	public void saveRefreshToken(String key, String token, long expirationTime) {
		redisTemplate.opsForValue().set(key, token, expirationTime, TimeUnit.MILLISECONDS);
	}

	public String getRefreshToken(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void deleteRefreshToken(String key) {
		redisTemplate.delete(key);
	}
}
