package com.avalon.avalonchat.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.PhoneNumberAuthenticationCode;
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
import com.avalon.avalonchat.domain.user.repository.PhoneNumberAuthenticationRepository;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;
import com.avalon.avalonchat.infra.message.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageService messageService;
	private final PhoneNumberAuthenticationRepository phoneNumberAuthenticationRepository;

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
		// 1. get phone number and certification code
		String phoneNumber = request.getPhoneNumber();
		String certificationCode = getCertificationCode();

		// 2. send certification code
		messageService.sendAuthenticationCode(phoneNumber, certificationCode);

		// 3. create phoneNumberAuthenticationCode
		PhoneNumberAuthenticationCode phoneNumberAuthenticationCode = new PhoneNumberAuthenticationCode(
			phoneNumber,
			certificationCode
		);

		// 4. save it
		phoneNumberAuthenticationRepository.save(phoneNumberAuthenticationCode);
	}

	@Override
	public PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request) {
		// 1. get phone number, certificationCode and phoneNumberAuthenticationCode
		String phoneNumber = request.getPhoneNumber();
		String certificationCode = request.getCertificationCode();

		// 2. check: if equals authenticate and return true, if not return false
		PhoneNumberAuthenticationCode phoneNumberAuthenticationCode = phoneNumberAuthenticationRepository.findById(
			phoneNumber).orElseThrow(() -> new AvalonChatRuntimeException("인증번호를 받지 않은 사용자입니다."));

		if (phoneNumberAuthenticationCode.getCertificationCode().equals(certificationCode)) {
			phoneNumberAuthenticationCode.authenticate();
			phoneNumberAuthenticationRepository.save(phoneNumberAuthenticationCode);
			return new PhoneNumberAuthenticationCheckResponse(true);
		}
		return new PhoneNumberAuthenticationCheckResponse(false);
	}

	private String getCertificationCode() {
		return RandomStringUtils.randomNumeric(6);
	}
}
