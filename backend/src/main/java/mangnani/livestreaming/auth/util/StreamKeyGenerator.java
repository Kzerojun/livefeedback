package mangnani.livestreaming.auth.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class StreamKeyGenerator {

	private static final int RANDOM_BYTES_LENGTH = 24;
	private static final String UUID_SEPARATOR = "-";
	private static final String EMPTY_STRING = "";

	// 스트림 키를 생성하는 메소드
	public static String generateStreamKey() {
		UUID uuid = UUID.randomUUID();
		SecureRandom random = new SecureRandom();
		byte[] randomBytes = new byte[RANDOM_BYTES_LENGTH];
		random.nextBytes(randomBytes);

		String uuidPart = uuid.toString().replace(UUID_SEPARATOR, EMPTY_STRING);
		String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

		return uuidPart + randomPart;
	}
}
