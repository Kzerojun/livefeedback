package mangnani.livestreaming.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import mangnani.livestreaming.global.jwt.provider.JwtTokenProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, String> redisTemplate;
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_TYPE = "Bearer ";
	private static final String BLACKLIST = "BL:";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		//토큰 추출
		String token = resolveToken(request);

		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}

		//유효성 검사
		if (jwtTokenProvider.validate(token)) {
			if (Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST + token))) {
				filterChain.doFilter(request, response);
				return;
			}

			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info(String.valueOf(authentication));

		}

		filterChain.doFilter(request, response);
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (bearerToken != null && StringUtils.hasText(AUTHORIZATION_HEADER)
				&& bearerToken.startsWith(
				BEARER_TYPE)) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
