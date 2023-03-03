package com.avalon.avalonchat.domain.login.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginServiceImpl implements LoginService {

	private final UserRepository userRepository;
	// private final TokenProvider tokenProvider;

	@Override
	public LoginResponse login(LoginRequest request) {

		return null;
	}
}
