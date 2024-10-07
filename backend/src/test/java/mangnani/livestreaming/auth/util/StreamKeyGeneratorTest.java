package mangnani.livestreaming.auth.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("스트림 키 생성기 테스트")
class StreamKeyGeneratorTest {

	@Test
	@DisplayName("생성된 스트림 키의 길이가 올바른지 확인")
	void testStreamKeyLength() {
		String streamKey = StreamKeyGenerator.generateStreamKey();
		// UUID(32) + Base64 encoded 24 bytes (32) = 64 characters
		assertEquals(64, streamKey.length(), "스트림 키의 길이는 64여야 합니다.");
	}

	@Test
	@DisplayName("생성된 스트림 키에 특수 문자가 없는지 확인")
	void testStreamKeyFormat() {
		String streamKey = StreamKeyGenerator.generateStreamKey();
		assertTrue(streamKey.matches("[A-Za-z0-9]+"), "스트림 키는 알파벳과 숫자로만 구성되어야 합니다.");
	}

	@RepeatedTest(100)
	@DisplayName("생성된 스트림 키의 유일성 확인")
	void testStreamKeyUniqueness() {
		String streamKey1 = StreamKeyGenerator.generateStreamKey();
		String streamKey2 = StreamKeyGenerator.generateStreamKey();
		assertNotEquals(streamKey1, streamKey2, "연속으로 생성된 스트림 키는 서로 달라야 합니다.");
	}

	@Test
	@DisplayName("대량의 스트림 키 생성 시 모두 유일한지 확인")
	void testMassiveStreamKeyGeneration() {
		Set<String> streamKeys = new HashSet<>();
		int generationCount = 10000;

		for (int i = 0; i < generationCount; i++) {
			String streamKey = StreamKeyGenerator.generateStreamKey();
			assertTrue(streamKeys.add(streamKey), "생성된 모든 스트림 키는 유일해야 합니다.");
		}

		assertEquals(generationCount, streamKeys.size(), "생성된 모든 스트림 키의 개수가 일치해야 합니다.");
	}
}