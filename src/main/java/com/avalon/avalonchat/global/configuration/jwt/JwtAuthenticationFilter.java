package com.avalon.avalonchat.global.configuration.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avalon.avalonchat.domain.login.exception.InvalidAuthorizationHeaderException;
import com.avalon.avalonchat.domain.user.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtUserDetailsService jwtUserDetailsService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String username = null;
		String jwtToken = null;
		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		log.info("authorizationHeader : {}", authorizationHeader);
		try {
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				throw new InvalidAuthorizationHeaderException();
			}
			jwtToken = authorizationHeader.substring(7);
			log.info("jwtToken : {}", jwtToken);
			username = jwtTokenProvider.getEmailfromAccessToken(jwtToken);
			log.info("username : {}", username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
				if (jwtTokenProvider.validateAccessToken(jwtToken)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
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
