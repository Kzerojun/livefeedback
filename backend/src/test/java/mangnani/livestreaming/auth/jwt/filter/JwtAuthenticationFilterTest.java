package mangnani.livestreaming.auth.jwt.filter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import mangnani.livestreaming.auth.jwt.extractor.TokenExtractor;
import mangnani.livestreaming.auth.jwt.service.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWT 인증 필터 테스트")
class JwtAuthenticationFilterTest {

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private TokenExtractor tokenExtractor;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	private static final String VALID_TOKEN = "valid_token";
	private static final String INVALID_TOKEN = "invalid_token";

	@BeforeEach
	void setUp() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("유효한 토큰으로 인증 성공")
	void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
		// Arrange
		when(tokenExtractor.resolveAccessToken(request)).thenReturn(VALID_TOKEN);
		when(jwtTokenProvider.validate(VALID_TOKEN)).thenReturn(true);
		when(jwtTokenProvider.getAuthentication(VALID_TOKEN)).thenReturn(authentication);

		// Act
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Assert
		verify(jwtTokenProvider).validate(VALID_TOKEN);
		verify(jwtTokenProvider).getAuthentication(VALID_TOKEN);
		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("토큰이 없을 때 필터 체인 계속 진행")
	void testDoFilterInternal_WithNoToken() throws ServletException, IOException {
		// Arrange
		when(tokenExtractor.resolveAccessToken(request)).thenReturn(null);

		// Act
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Assert
		verify(filterChain).doFilter(request, response);
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	@DisplayName("유효하지 않은 토큰으로 인증 실패")
	void testDoFilterInternal_WithInvalidToken() throws ServletException, IOException {
		// Arrange
		when(tokenExtractor.resolveAccessToken(request)).thenReturn(INVALID_TOKEN);
		when(jwtTokenProvider.validate(INVALID_TOKEN)).thenReturn(false);
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);

		// Act
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Assert
		verify(jwtTokenProvider).validate(INVALID_TOKEN);
		verify(response).setContentType("application/json;charset=UTF-8");
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		assertTrue(stringWriter.toString().contains("UNAUTHORIZED_KEY"));
		assertNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain, never()).doFilter(request, response);
	}
}