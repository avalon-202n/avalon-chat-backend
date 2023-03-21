package com.avalon.avalonchat.global.configuration.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private JwtAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public static JwtAuthenticationToken of(String jwt) {
		return new JwtAuthenticationToken(jwt, jwt);
	}
}
