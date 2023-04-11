package com.avalon.avalonchat.global.configuration.jwt;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.avalon.avalonchat.global.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private JwtTokenService jwtTokenService;

	public JwtAuthenticationFilter(
		JwtTokenService jwtTokenService,
		RequestMatcher matcher,
		AuthenticationManager authenticationManager
	) {
		super(matcher);
		super.setAuthenticationManager(authenticationManager);
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public Authentication attemptAuthentication(
		HttpServletRequest req,
		HttpServletResponse res
	) {
		String token = req.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		final JwtAuthenticationToken authentication = JwtAuthenticationToken.of(token);
		return getAuthenticationManager().authenticate(authentication);
	}

	// TODO - can be removed?
	@Override
	protected void successfulAuthentication(
		HttpServletRequest req,
		HttpServletResponse res,
		FilterChain chain,
		Authentication auth
	) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(auth);
		chain.doFilter(req, res);
	}

	// TODO - introduce authenticationFailureHandler and apply @Here and CustomAuthenticationEntryPoint?
	@Override
	protected void unsuccessfulAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException failed
	) throws IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		try (OutputStream os = response.getOutputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(os, new ErrorResponse(HttpStatus.FORBIDDEN, failed));
			os.flush();
		}
	}
}
