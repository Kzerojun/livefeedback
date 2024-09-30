package mangnani.livestreaming.auth.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenValidator {

	@Value("${spring.jwt.secret}")
	private String jwtSecret;

	public boolean validate(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
			log.error("Invalid token type: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("Expired token: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported token: {}", e.getMessage());
		} catch (Exception e) {
			log.error("Unknown error occurred: {}", e.getMessage());
		}
		return false;
	}
}
