package mangnani.livestreaming.global.jwt.provider;

import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.auth.dto.response.SignInResponse;
import mangnani.livestreaming.global.exception.NoPermissionTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Component
@Slf4j
public class JwtTokenProvider {

	private static final String AUTHORITIES_KEY = "auth";
	private static final String BEARER_TYPE = "Bearer";

	@Value("${spring.jwt.secret}")
	private String jwtSecret;

	@Value("${spring.jwt.token.access-expiration-time}")
	private long accessTokenExpireTime;

	@Value("${spring.jwt.token.refresh-expiration-time}")
	private long refreshTokenExpireTime;


	public SignInResponse generateToken(Authentication authentication) {
		String authorities = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		//Access Token 생성
		Date accessTokenExpiresIn = Date.from(Instant.now().plusMillis(accessTokenExpireTime));
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.setExpiration(accessTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS256, jwtSecret)
				.compact();

		Date refreshTokenExpiresIn = Date.from(Instant.now().plusMillis(refreshTokenExpireTime));
		String refreshToken = Jwts.builder()
				.setExpiration(refreshTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS256, jwtSecret)
				.compact();

		return SignInResponse.builder()
				.grantType(BEARER_TYPE)
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.refreshTokenExpirationTIme(refreshTokenExpireTime)
				.build();
	}

	public boolean validate(String accessToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(accessToken);
			return true;
		} catch (SecurityException | MalformedJwtException exception) {
			log.info("Invalid JWT Token", exception);
		} catch (ExpiredJwtException exception) {
			log.info("Expired JWT Token");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}

		return false;
	}


	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		System.out.println(claims);
		if (claims.get(AUTHORITIES_KEY) == null) {
			throw new NoPermissionTokenException();
		}

		Collection<? extends GrantedAuthority> authorities = Arrays.stream(
						claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();

		User principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public Long getExpiration(String accessToken) {
		Date expiration = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(accessToken)
				.getBody()
				.getExpiration();
		return expiration.getTime() - new Date().getTime();
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parser()
					.setSigningKey(jwtSecret)
					.parseClaimsJws(accessToken)
					.getBody();
		} catch (ExpiredJwtException exception) {
			return exception.getClaims();
		}
	}

}
