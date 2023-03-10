package com.avalon.avalonchat.global.configuration.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avalon.avalonchat.domain.login.exception.InvalidAuthorizationHeaderException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final JwtTokenService jwtTokenService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String userId = null;
		String jwtToken = null;
		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		log.info("authorizationHeader : {}", authorizationHeader);
		try {
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				throw new InvalidAuthorizationHeaderException();
			}
			jwtToken = authorizationHeader.substring(7);
			log.info("jwtToken : {}", jwtToken);
			userId = jwtTokenService.getUserIdFromAccessToken(jwtToken);
			log.info("userId : {}", userId);
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if (jwtTokenService.validateToken(jwtToken)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userId, jwtToken, null);
					authenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		} catch (InvalidAuthorizationHeaderException e) {
			request.setAttribute("errorCode", "InvalidAuthorizationHeaderException");
		} catch (ExpiredJwtException e) {
			log.warn("JWT Access Token has expired");
			request.setAttribute("errorCode", "ExpiredJwtException");
		} catch (JwtException e) {
			log.warn("Unable to get JWT Token");
			request.setAttribute("errorCode", "JwtException");
		}
		filterChain.doFilter(request, response);
	}
}
