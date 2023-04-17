package com.avalon.avalonchat.global.configuration.jwt;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.avalon.avalonchat.domain.model.SecurityUser;

public class Jwt extends UsernamePasswordAuthenticationToken {

	/**
	 * 인증되지 않은 Jwt 생성
	 */
	private Jwt(Object principal, Object credentials) {
		super(principal, credentials);
	}

	/**
	 * 인증된 Jwt 생성
	 */
	public Jwt(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public static Jwt ofToken(String token) {
		return new Jwt(token, null);
	}

	public static Jwt ofAuthenticated(SecurityUser securityUser) {
		return new Jwt(securityUser, null, AuthorityUtils.NO_AUTHORITIES);
	}
}
