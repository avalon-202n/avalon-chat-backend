package com.avalon.avalonchat.global.configuration.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.avalon.avalonchat.domain.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class JwtTokenProvider {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	@Value("${jwt.access.validity}")
	private static long ACCESS_TOKEN_VALIDITY;
	@Value("${jwt.refresh.validity}")
	private static long REFRESH_TOKEN_VALIDITY;
	private final Key secretKey;

	public JwtTokenProvider(@Value("${jwt.key}") byte[] secretKey) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey);
	}

	public String getUserIdFromAccessToken(String token) {
		return getClaimFromAccessToken(token);
	}

	public String getClaimFromAccessToken(String token) {
		Claims claims = getAllClaimsFromAccessToken(token);
		return claims.get("userId").toString();
	}

	private Claims getAllClaimsFromAccessToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	public String doGenerateRefreshToken() {
		long currentTime = (new Date()).getTime();
		final Date refreshTokenExpiresIn = new Date(currentTime + REFRESH_TOKEN_VALIDITY);

		return Jwts.builder()
			.setSubject("RefreshToken")
			.setExpiration(refreshTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public String doGenerateAccessToken(User user) {
		long currentTime = (new Date()).getTime();
		final Date accessTokenExpiresIn = new Date(currentTime + ACCESS_TOKEN_VALIDITY);

		return Jwts.builder()
			.setSubject("AccessToken")
			.claim("userId", user.getId())
			.claim("email", user.getEmail())
			.setExpiration(accessTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public Boolean validateAccessToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			return false;
		} catch (JwtException e) {
			return false;
		}
		return true;
	}
}
