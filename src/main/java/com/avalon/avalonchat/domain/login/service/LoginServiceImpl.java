package com.avalon.avalonchat.domain.login.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.exception.LoginFailedException;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	private final UserRepository userRepository;
	private final GetProfileIdService getProfileIdService;
	private final TokenService tokenService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public LoginResponse login(LoginRequest request) {
		// 1. check user exists
		final User findUser = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new LoginFailedException("email.notfound", request.getEmail()));

		// 2. verify password
		if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword().getValue())) {
			throw new LoginFailedException("password.mismatch");
		}

		long profileId = getProfileIdService.getProfileIdByUserId(findUser.getId());

		// 3. jwt token create
		final String accessToken = tokenService.createAccessToken(findUser, profileId);
		return new LoginResponse(findUser.getEmail(), accessToken);
	}
}
