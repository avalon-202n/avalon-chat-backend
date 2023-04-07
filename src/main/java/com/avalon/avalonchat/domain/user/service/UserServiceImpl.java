package com.avalon.avalonchat.domain.user.service;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.dto.*;
import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public SignUpResponse signUp(SignUpRequest signUpRequest) {
		// create user from request
		User user = signUpRequest.toEntity();

		// TODO - password vo 도입 및 encoding 적용

		// save user
		User savedUser = userRepository.save(user);

		// convert to response
		return SignUpResponse.ofEntity(savedUser);
	}

	@Override
	public EmailDuplicatedCheckResponse checkEmailDuplicated(EmailDuplicatedCheckRequest request) {
		Email email = request.getEmail();

		boolean exists = userRepository.existsByEmail(email);

		return new EmailDuplicatedCheckResponse(exists);
	}

	@Override
	public void sendEmailAuthentication(EmailAuthenticationSendRequest request) {
		Email email = request.getEmail();

		// 2. send email
		// emailService.sendAuthCode(signUpId, email); ??
	}

	@Override
	public EmailAuthenticationCheckResponse checkEmailAuthentication(EmailAuthenticationCheckRequest request) {
		Email email = request.getEmail();
		String certificationCode = request.getCertificationCode();

		// check exists
		// boolean exists = redisTemplate/emailAuthRepository.existsBy(signUpId, email. certificationCode); ??
		boolean exist = false;

		return new EmailAuthenticationCheckResponse(exist);
	}

	@Override
	public void sendPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request) {
		String phoneNumber = request.getPhoneNumber();

		// send
		// phoneMessageService.sendAuthCode(signUpId, phoneNumber); ??
	}

	@Override
	public PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(PhoneNumberAuthenticationCheckRequest request) {
		String phoneNumber = request.getPhoneNumber();
		String certificationCode = request.getCertificationCode();

		// check
		// boolean exists = redisTemplate/phoneNumberAuthRepository.existsBy(signUpId, phoneNumber, certificationCode); ??
		boolean exist = false;

		return new PhoneNumberAuthenticationCheckResponse(exist);
	}
}
