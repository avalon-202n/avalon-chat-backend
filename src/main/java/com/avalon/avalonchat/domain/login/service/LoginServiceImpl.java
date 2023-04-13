package com.avalon.avalonchat.domain.login.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.exception.LoginInvalidInputException;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	private final UserRepository userRepository;
	private final JwtTokenService jwtTokenService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public LoginResponse login(LoginRequest request) {
		// 1. check user exsits
		final User findUser = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new LoginInvalidInputException("일치하는 이메일이 존재하지 않습니다."));

		// 2. verify password
		if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword().getValue())) {
			throw new LoginInvalidInputException("비밀번호가 일치하지 않습니다.");
		}

		// 3. jwt token create
		final String accessToken = jwtTokenService.createAccessToken(findUser);
		return new LoginResponse(findUser.getEmail(), accessToken);
	}
}
