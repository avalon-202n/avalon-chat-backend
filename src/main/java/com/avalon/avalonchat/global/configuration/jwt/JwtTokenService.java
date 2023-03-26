package com.avalon.avalonchat.global.configuration.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.avalon.avalonchat.domain.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenService {
	public static final String ACCESS_TOKEN = "AccessToken";
	public static final String REFRESH_TOKEN = "RefreshToken";

	private Key secretKey;
	private JwtConfigProperties jwtConfigProperties;

	@Autowired
	public JwtTokenService(JwtConfigProperties jwtConfigProperties) {
		this.jwtConfigProperties = jwtConfigProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtConfigProperties.getKey().getBytes());
	}

	/**
	 * Refresh Token 발급
	 * @param user
	 * @return refreshToken
	 */
	public String doGenerateRefreshToken(User user) {
		long currentTime = (new Date()).getTime();
		final Date refreshTokenExpiresIn = new Date(currentTime + jwtConfigProperties.getRefreshValidity());

		return Jwts.builder()
			.setSubject(REFRESH_TOKEN)
			.setExpiration(refreshTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 * Access Token 발급
	 * @param user
	 * @return accessToken
	 */
	public String doGenerateAccessToken(User user) {
		long currentTime = (new Date()).getTime();
		final Date accessTokenExpiresIn = new Date(currentTime + jwtConfigProperties.getAccessValidity());

		return Jwts.builder()
			.setSubject(ACCESS_TOKEN)
			.claim("userId", user.getId())
			.claim("email", user.getEmail().getValue())
			.setExpiration(accessTokenExpiresIn)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 *
	 * @param token
	 * @return
	 */
	public String getUserIdFromAccessToken(String token) {
		return getClaimFromToken(token, Claims::getId);
	}

	/**
	 * token으로 사용자 속성 정보 조회
	 * @param token
	 * @param claimsResolver
	 * @param <T>
	 * @return
	 */
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Claims getTokenClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SecurityException e) {
			log.error("Invalid JWT signature.");
			throw new JwtException("잘못된 JWT 시그니처");
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token.");
			throw new JwtException("유효하지 않은 JWT 토큰");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token.");
			throw new JwtException("토큰 기한 만료");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			log.error("JWT token compact of handler are invalid.");
			throw new JwtException("JWT token compact of handler are invalid.");
		}
		return null;
	}
}
