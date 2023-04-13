package com.avalon.avalonchat.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public SignUpResponse signUp(SignUpRequest signUpRequest) {
		// create user from request
		User user = new User(
			signUpRequest.getEmail(),
			signUpRequest.getPassword()
		);

		//password encode
		String encryptedPassword = passwordEncoder.encode(signUpRequest.getPassword().getValue());
		user.setEncryptedPassword(encryptedPassword);

		// TODO - check email authenticated via redis

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
		// boolean exists
		//  	= redisTemplate/emailAuthRepository.existsBy(signUpId, email. certificationCode); ??
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
	public PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request) {
		String phoneNumber = request.getPhoneNumber();
		String certificationCode = request.getCertificationCode();

		// check
		// boolean exists
		// 		= redisTemplate/phoneNumberAuthRepository.existsBy(signUpId, phoneNumber, certificationCode); ??
		boolean exist = false;

		return new PhoneNumberAuthenticationCheckResponse(exist);
	}
}
