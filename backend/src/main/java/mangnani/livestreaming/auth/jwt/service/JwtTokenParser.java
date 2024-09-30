package mangnani.livestreaming.auth.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.global.config.CustomUserDetailsService;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenParser {

	@Value("${spring.jwt.secret}")
	private String jwtSecret;

	private final CustomUserDetailsService userDetailsService;
	public Claims parseClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException exception) {
			return exception.getClaims();
		}
	}

	public Long getExpiration(String token) {
		Date expiration = parseClaims(token).getExpiration();
		return expiration.getTime() - new Date().getTime();
	}

	// JWT 토큰에서 인증 정보 추출
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		if (claims.get("auth") == null) {
			throw new NoPermissionTokenException();
		}

		// JWT 토큰의 주체(subject)를 이용하여 UserDetails 불러오기
		UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

		return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
	}
}
