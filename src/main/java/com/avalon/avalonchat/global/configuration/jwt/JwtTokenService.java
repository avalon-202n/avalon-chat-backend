package com.avalon.avalonchat.global.configuration.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.avalon.avalonchat.domain.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenService {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	private Key secretKey;
	private JwtConfigProperties jwtConfigProperties;

	@Autowired
	public JwtTokenService(JwtConfigProperties jwtConfigProperties) {
		this.jwtConfigProperties = jwtConfigProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtConfigProperties.getSecretKey().getBytes());
	}

	public String getUserIdFromAccessToken(String token) {
		Claims claims = getAllClaimsFromAccessToken(token);
		return claims.get("userId").toString();
	}

	public String getEmailFromAccessToken(String token) {
		Claims claims = getAllClaimsFromAccessToken(token);
		return claims.get("email").toString();
	}

	private Claims getAllClaimsFromAccessToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	public String doGenerateRefreshToken(UserDetails userDetails) {
		long currentTime = (new Date()).getTime();
		final Date refreshTokenExpiresIn = new Date(currentTime + jwtConfigProperties.getRefreshValidity());

		return Jwts.builder()
			.setSubject("RefreshToken")
			.setExpiration(refreshTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public String doGenerateAccessToken(User user) {
		long currentTime = (new Date()).getTime();
		final Date accessTokenExpiresIn = new Date(currentTime + jwtConfigProperties.getAccessValidity());
		return Jwts.builder()
			.setSubject("AccessToken")
			.claim("userId", user.getId())
			.claim("email", user.getEmail().getValue())
			.setExpiration(accessTokenExpiresIn)
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
		} catch (ExpiredJwtException e) {
			return false;
		} catch (JwtException e) {
			return false;
		}
		return true;
	}
}
