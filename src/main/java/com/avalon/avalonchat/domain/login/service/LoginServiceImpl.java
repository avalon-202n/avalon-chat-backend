package com.avalon.avalonchat.domain.login.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.EmailFindResponse;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.domain.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.domain.login.exception.LoginInvalidInputException;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
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

	private final ProfileRepository profileRepository;
	private final GetProfileIdService getProfileIdService;
	private final JwtTokenService jwtTokenService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public LoginResponse login(LoginRequest request) {
		// 1. check user exists
		User findUser = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new LoginInvalidInputException("일치하는 이메일이 존재하지 않습니다."));

		// 2. verify password
		if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword().getValue())) {
			throw new LoginInvalidInputException("비밀번호가 일치하지 않습니다.");
		}

		long profileId = getProfileIdService.getProfileIdByUserId(findUser.getId());

		// 3. jwt token create
		String accessToken = jwtTokenService.createAccessToken(findUser, profileId);
		return new LoginResponse(findUser.getEmail(), accessToken);
	}

	@Override
	public EmailFindResponse findEmailByPhoneNumber(String phoneNumber) {
		Email email = profileRepository.findByPhoneNumber(phoneNumber)
			.orElseThrow(() -> new RuntimeException("일치하는 계정이 없습니다."));
		return new EmailFindResponse(findProfile.getUser().getEmail());
	}

	@Override
	public PasswordFindResponse resetPassword(PasswordFindRequest request) {
		//TODO 이메일 인증 하기
		//TODO 비밀번호 찾기 설계필요 (임시 비밀번호 발급 & 비밀 번호 재설정)
		return new PasswordFindResponse(Password.of("password"));
	}
}
