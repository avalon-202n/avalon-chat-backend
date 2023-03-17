package com.avalon.avalonchat.global.configuration.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.avalon.avalonchat.domain.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class JwtTokenService {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	private static final long ACCESS_TOKEN_VALIDITY = 1800000;  //30분
	private static final long REFRESH_TOKEN_VALIDITY = 1209600000;  //2주

	private final Key secretKey;

	//private final JwtConfigProperties jwtConfigProperties;

	public JwtTokenService() {
		this.secretKey = Keys.hmacShaKeyFor(
			"dlsfjslfjdlsfjiesldlsfmjldsfjsdofjsldfjsld".getBytes(StandardCharsets.UTF_8));
	}

	public String getUserIdFromAccessToken(String token) {
		Claims claims = getAllClaimsFromAccessToken(token);
		return claims.get("userId").toString();
	}

	private Claims getAllClaimsFromAccessToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	public String doGenerateRefreshToken(UserDetails userDetails) {
		long currentTime = (new Date()).getTime();
		final Date refreshTokenExpiresIn = new Date(currentTime + ACCESS_TOKEN_VALIDITY);

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
