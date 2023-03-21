package com.avalon.avalonchat.global.configuration.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
	private final JwtTokenService jwtTokenService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String token = (String)authentication.getPrincipal();
		if (jwtTokenService.validateToken(token)) {
			return authentication;
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return JwtAuthenticationToken.class.isAssignableFrom(aClass);
	}

}
