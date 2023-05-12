package com.avalon.avalonchat.infra.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import com.avalon.avalonchat.configuration.jwt.JwtProperties;
import com.avalon.avalonchat.core.login.application.TokenService;
import com.avalon.avalonchat.core.user.domain.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JwtTokenService
 * Jwt Token 생성 및 검증을 담당한다.
 */
public class JwtTokenService implements TokenService {

	private final JwtParser jwtParser;
	private long accessValidity;
	private long refreshValidity;
	private Key secretKey;

	public JwtTokenService(JwtProperties properties) {
		this.accessValidity = properties.getAccessValidity();
		this.refreshValidity = properties.getRefreshValidity();
		this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
		this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
	}

	@Override
	public Map<String, Object> parseClaim(String token) {
		return jwtParser
			.parseClaimsJws(token)
			.getBody();
	}

	@Override
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

	@Override
	public String createRefreshToken() {
		long currentTime = (new Date()).getTime();
		final Date refreshTokenExpiresIn = new Date(currentTime + refreshValidity);

		return Jwts.builder()
			.setSubject("RefreshToken")
			.setExpiration(refreshTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	@Override
	public boolean isExpired(String token) {
		try {
			parseClaim(token);
		} catch (ExpiredJwtException e) {
			return true;
		}
		return false;
	}
}
