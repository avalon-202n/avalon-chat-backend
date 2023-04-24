package com.avalon.avalonchat.domain.user.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.avalon.avalonchat.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageService messageService;
	private final PhoneNumberAuthenticationRepository phoneNumberAuthenticationRepository;

	@Transactional
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
		String certificationCode = RandomStringUtils.randomNumeric(6);

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
		PhoneNumberAuthenticationCheckRequest request
	) {
		// 1. binding
		String phoneNumber = request.getPhoneNumber();
		String certificationCode = request.getCertificationCode();

		// 2. find auth-code and check cert-code matches
		PhoneNumberAuthenticationCode phoneNumberAuthenticationCode = phoneNumberAuthenticationRepository
			.findById(phoneNumber)
			.orElseThrow(() -> new NotFoundException("phonenumber.auth-code", phoneNumber));
		boolean authenticated = phoneNumberAuthenticationCode.getCertificationCode().equals(certificationCode);

		// 3. do post process and return
		if (authenticated) {
			phoneNumberAuthenticationCode.authenticate();
			phoneNumberAuthenticationRepository.save(phoneNumberAuthenticationCode);
		}
		return new PhoneNumberAuthenticationCheckResponse(authenticated);
	}
}
