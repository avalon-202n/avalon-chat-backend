package com.avalon.avalonchat.core.user.application;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.core.user.dto.SignUpResponse;
import com.avalon.avalonchat.core.user.keyvalue.KeyAuthCodeValueStore;
import com.avalon.avalonchat.core.user.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SmsMessageService smsMessageService;
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
	public void sendPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request) {
		// 1. get phone number and certification code
		String phoneNumber = request.getPhoneNumber().replaceAll("-", "").trim();
		String certificationCode = RandomStringUtils.randomNumeric(6);

		// 2. send certification code
		smsMessageService.sendAuthenticationCode(phoneNumber, certificationCode);

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
		// 1. get phone number
		String phoneNumber = request.getPhoneNumber().replaceAll("-", "").trim();

		// 2. check authenticated
		boolean authenticated = phoneNumberKeyValueStore.checkKeyValueMatches(
			PhoneNumberKey.fromString(phoneNumber),
			request.getCertificationCode()
		);

		// 3. return
		return new PhoneNumberAuthenticationCheckResponse(authenticated);
	}
}
