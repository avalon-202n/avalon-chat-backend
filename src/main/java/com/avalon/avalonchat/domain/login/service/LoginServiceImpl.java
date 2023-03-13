package com.avalon.avalonchat.domain.login.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public LoginResponse login(LoginRequest request) {
		//TODO
		// 1. check user exsits
		// 2. verify password
		return null;
	}

	public String createAccessTokenByEmail(String email) {
		final User findUser = userRepository.findByEmail(Email.of(email))
			.orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없었습니다."));
		return jwtTokenProvider.doGenerateAccessToken(findUser);
	}
}
