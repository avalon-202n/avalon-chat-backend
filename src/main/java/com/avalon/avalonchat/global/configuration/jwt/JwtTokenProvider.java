package com.avalon.avalonchat.global.configuration.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Component
public class JwtTokenProvider {
	@Value("${jwt.access.validity}")
	private long ACCESS_TOKEN_VALIDITY;
	@Value("${jwt.refresh.validity}")
	private long REFRESH_TOKEN_VALIDITY;

	private final Key accessKey;
	private final Key refreshKey;

	public JwtTokenProvider(@Value("${jwt.access.secret}") String accessSecret, @Value("${jwt.refresh.secret}") String refreshSecret) {
		byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecret);
		byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecret);
		this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
		this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
	}

	/**
	 *
	 * @param userDetails
	 * @return
	 */
	public String generateAccessToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "AccessToken");
		return doGenerateToken(claims, headers, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY, accessKey);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "RefreshToken");
		return doGenerateToken(claims, headers, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY, refreshKey);
	}

	public String doGenerateToken(Map<String, Object> claims, Map<String, Object> headers, String subject, long validity, Key key) {
		String jws = Jwts.builder()
						.setHeader(headers)
						.setClaims(claims)
						.setIssuedAt(new Date(System.currentTimeMillis()))
						.setExpiration(new Date(System.currentTimeMillis() + validity))
						.setSubject(subject)
						.signWith(key, SignatureAlgorithm.HS256)
						.compact();
		log.info("jws : {}", jws);
		return jws;
	}

	public Boolean validateAccessToken(String token) {
		return validateToken(token, accessKey);
	}

	public void validateRefreshToken(String token) {
		if (!validateToken(token, refreshKey)) {
			//throw new ExpiredRefreshTokenException();
		}
	}

	private Boolean validateToken(String token, Key secret) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secret)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			return false;
		} catch (JwtException e) {
			//예외 처리
		}
		return true;
	}
}
