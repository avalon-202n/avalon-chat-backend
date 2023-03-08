package com.avalon.avalonchat.domain.login.service;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.service.JwtUserDetailsService;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	private final UserRepository userRepository;
	private final JwtUserDetailsService jwtUserDetailsService;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public LoginResponse login(LoginRequest request) {
		//TODO
		// 1. check user exsits
		// 2. verify password
		return null;
	}

	public String createAccessTokenByEmail(String email) {
		final User findUser = userRepository.findByEmail(Email.of(email));

		log.info("email : {}", findUser.getEmail().toString());
		log.info("password : {}", findUser.getPassword().toString());
		log.info("create_at : {}", findUser.getCreatedAt().toString());
		log.info("modified_at : {}", findUser.getUpdatedAt().toString());

		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
		return jwtTokenProvider.generateAccessToken(userDetails);
	}
}
