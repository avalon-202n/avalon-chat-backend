package com.avalon.avalonchat.domain.user.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.avalon.avalonchat.domain.user.keyvalue.EmailKey;
import com.avalon.avalonchat.domain.user.keyvalue.KeyAuthCodeValueStore;
import com.avalon.avalonchat.domain.user.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageService messageService;
	private final KeyAuthCodeValueStore<EmailKey> emailKeyValueStore;
	private final KeyAuthCodeValueStore<PhoneNumberKey> phoneNumberKeyValueStore;

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
	}

	@Override
	public EmailAuthenticationCheckResponse checkEmailAuthentication(EmailAuthenticationCheckRequest request) {
		boolean exist = false;
		return new EmailAuthenticationCheckResponse(exist);
	}

	@Override
	public void sendPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request) {
		// 1. get phone number and certification code
		String phoneNumber = request.getPhoneNumber().replaceAll("-", "").trim();
		String certificationCode = RandomStringUtils.randomNumeric(6);

		// 2. send certification code
		messageService.sendAuthenticationCode(phoneNumber, certificationCode);

		// 3. put it to key-value store
		phoneNumberKeyValueStore.put(
			PhoneNumberKey.fromString(phoneNumber),
			certificationCode
		);
	}

	@Override
	public PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request
	) {
		// 1. get key
		PhoneNumberKey phoneNumberKey = PhoneNumberKey.fromString(request.getPhoneNumber());

		// 2. check authenticated
		boolean authenticated = phoneNumberKeyValueStore.getAndPutIfAuthenticated(phoneNumberKey);

		// 3. return
		return new PhoneNumberAuthenticationCheckResponse(authenticated);
	}
}
