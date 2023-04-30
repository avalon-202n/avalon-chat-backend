package com.avalon.avalonchat.global.configuration.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.avalon.avalonchat.domain.login.service.TokenService;
import com.avalon.avalonchat.domain.user.domain.User;

import io.jsonwebtoken.JwtParser;
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
public class JwtTokenService implements TokenService {

	private final JwtParser jwtParser;
	private long accessValidity;
	private long refreshValidity;
	private Key secretKey;

	@Autowired
	public JwtTokenService(JwtConfigProperties properties) {
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
}
