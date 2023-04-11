package com.avalon.avalonchat.global.configuration.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtTokenService
 * Jwt Token 생성 및 검증을 담당한다.
 */
@Slf4j
@Component
public class JwtTokenService {

	private long accessValidity;
	private long refreshValidity;
	private Key secretKey;

	@Autowired
	public JwtTokenService(JwtConfigProperties properties) {
		this.accessValidity = properties.getAccessValidity();
		this.refreshValidity = properties.getRefreshValidity();
		this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
	}

	public Long getUserIdFromAccessToken(String token) {
		Claims claims = getAllClaimsFromAccessToken(token);
		return Long.parseLong(claims.get("userId").toString());
	}

	public Email getEmailFromAccessToken(String token) {
		Claims claims = getAllClaimsFromAccessToken(token);
		return Email.of(claims.get("email").toString());
	}

	private Claims getAllClaimsFromAccessToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	public String createAccessToken(User user, long profileId) {
		long currentTime = (new Date()).getTime();
		final Date accessTokenExpiresIn = new Date(currentTime + accessValidity);

		return Jwts.builder()
			.setSubject("AccessToken")
			.claim("userId", user.getId())
			.claim("profileId", profileId)
			.claim("email", user.getEmail().getValue())
			.setExpiration(accessTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public String createRefreshToken(User user) {
		long currentTime = (new Date()).getTime();
		final Date refreshTokenExpiresIn = new Date(currentTime + refreshValidity);

		return Jwts.builder()
			.setSubject("RefreshToken")
			.setExpiration(refreshTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public Boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (JwtException e) {
			return false;
		}
		return true;
	}
}
