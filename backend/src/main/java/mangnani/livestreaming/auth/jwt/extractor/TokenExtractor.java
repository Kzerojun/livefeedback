package mangnani.livestreaming.auth.jwt.extractor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Component
public class TokenExtractor {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_TYPE = "Bearer ";
	private static final String REFRESH_TYPE = "Refresh_Token";

	public String resolveAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (bearerToken != null && StringUtils.hasText(AUTHORIZATION_HEADER)
				&& bearerToken.startsWith(
				BEARER_TYPE)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public String resolveRefreshToken(HttpServletRequest request) {
		return request.getHeader(REFRESH_TYPE);
	}
}
