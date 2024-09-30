package mangnani.livestreaming.auth.jwt.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/*
	토큰 생성만 담당
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFactory {

	@Value("${spring.jwt.secret}")
	private String jwtSecret;

	@Value("${spring.jwt.token.access-expiration-time}")
	private long accessTokenExpireTime;

	@Value("${spring.jwt.token.refresh-expiration-time}")
	private long refreshTokenExpireTime;

	private static final String AUTHORITIES_KEY = "auth";

	public String generateAccessToken(Authentication authentication) {
		String authorities = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Date accessTokenExpiresIn = Date.from(Instant.now().plusMillis(accessTokenExpireTime));

		return Jwts.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.setExpiration(accessTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS256, jwtSecret)
				.compact();
	}

	public String generateRefreshToken() {
		Date refreshTokenExpiresIn = Date.from(Instant.now().plusMillis(refreshTokenExpireTime));

		return Jwts.builder()
				.setExpiration(refreshTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS256, jwtSecret)
				.compact();
	}
}
