package com.avalon.avalonchat.domain.login.service;

import com.avalon.avalonchat.domain.login.exception.LoginInvalidInputException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.user.domain.Email;
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
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public LoginResponse login(LoginRequest request) {
		final User findUser = userRepository.findByEmail(Email.of(request.getEmail()))
			.orElseThrow(() -> new LoginInvalidInputException("일치하는 이메일이 존재하지 않습니다."));
		if (!bCryptPasswordEncoder.matches(request.getPassword(), findUser.getPassword().getValue()))
			throw new LoginInvalidInputException("비밀번호가 일치하지 않습니다.");
		return new LoginResponse(findUser.getEmail().getValue(), jwtTokenService.doGenerateAccessToken(findUser));
	}

	public String createAccessTokenByEmail(String email) {
		final User findUser = userRepository.findByEmail(Email.of(email))
			.orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없었습니다."));
		return jwtTokenService.doGenerateAccessToken(findUser);
	}
}
