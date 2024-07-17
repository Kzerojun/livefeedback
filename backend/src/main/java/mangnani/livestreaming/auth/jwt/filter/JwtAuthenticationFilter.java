package mangnani.livestreaming.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.auth.jwt.extractor.TokenExtractor;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.auth.jwt.provider.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenExtractor tokenExtractor;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		// 토큰 추출
		String token = tokenExtractor.resolveAccessToken(request);
		if (token == null) {
			filterChain.doFilter(request,response);
			return;
		}

		if (jwtTokenProvider.validate(token)) {
			// 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info("Token validation succeeded.");
		} else {
			setResponse(response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void setResponse(HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ResponseDto responseDto = new ResponseDto(ResponseCode.UNAUTHORIZED_KEY,
				ResponseMessage.UNAUTHORIZED_KEY);
		String jsonString = new ObjectMapper().writeValueAsString(responseDto);

		response.getWriter().print(jsonString);
	}
}
