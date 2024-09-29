package mangnani.livestreaming.auth.jwt.extractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenExtractorTest {

	private TokenExtractor tokenExtractor;

	@Mock
	private HttpServletRequest request;

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private final String BEARER_VALID_ACCESS_TOKEN = "Bearer validAccessToken";

	@BeforeEach
	void setup() {
		tokenExtractor = new TokenExtractor();
	}

	@Test
	@DisplayName("올바른 베어러 토큰이 들어왔을 때 - 성공")
	void resolveAccessToken_success() {
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(BEARER_VALID_ACCESS_TOKEN);
		String accessToken = tokenExtractor.resolveAccessToken(request);
		assertThat(accessToken).isEqualTo("validAccessToken");
	}

	@Test
	@DisplayName("Authorization 헤더가 없는 경우 - 실패")
	void resolveAccessToken_failNoAuthorizationHeader() {
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(null);
		String accessToken = tokenExtractor.resolveAccessToken(request);
		assertThat(accessToken).isNull();
	}

	@Test
	@DisplayName("인증방식이 Bearer가 아닌경우 - 실패")
	void resolveAccessToken_failNotBearerToken() {
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn("InvalidToken");
		String accessToken = tokenExtractor.resolveAccessToken(request);
		assertThat(accessToken).isNull();
	}

	@Test
	void testResolveRefreshToken_successValidRefreshToken() {
		when(request.getHeader("Refresh_Token")).thenReturn("validRefreshToken");

		// 메서드 호출 및 결과 검증
		String refreshToken = tokenExtractor.resolveRefreshToken(request);
		assertEquals("validRefreshToken", refreshToken);
	}

	@Test
	void testResolveRefreshToken_withNoRefreshToken() {
		when(request.getHeader("Refresh_Token")).thenReturn(null);

		String refreshToken = tokenExtractor.resolveRefreshToken(request);
		assertNull(refreshToken);
	}

}