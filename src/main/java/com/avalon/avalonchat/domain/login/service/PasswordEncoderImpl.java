package com.avalon.avalonchat.domain.login.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordEncoderImpl implements PasswordEncoder {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public String encode(String password) {
		return bCryptPasswordEncoder.encode(password);
	}

	@Override
	public boolean matches(String password, String encodedPassword) {
		return bCryptPasswordEncoder.matches(password, encodedPassword);
	}

}
