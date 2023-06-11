package com.avalon.avalonchat.core.user.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.profile.application.ProfileService;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.core.user.dto.SignUpResponse;
import com.avalon.avalonchat.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ProfileService profileService;
	private final SmsMessageService smsMessageService;
	private final PhoneNumberAuthCodeStore phoneNumberKeyValueStore;

	@Transactional
	@Override
	public SignUpResponse signUp(SignUpRequest signUpRequest) {

		// 1. phoneNumber check authenticated
		String phoneNumber = signUpRequest.getPhoneNumber().getValue();
		boolean authenticated = phoneNumberKeyValueStore.isAuthenticated(PhoneNumberKey.fromString(phoneNumber));
		log.info("phoneNumber : {}", PhoneNumberKey.fromString(phoneNumber));
		log.info("authenticated : {}", phoneNumberKeyValueStore.isAuthenticated(PhoneNumberKey.fromString(phoneNumber)));
		if (!authenticated) {
			throw new BadRequestException("phonenumber.no-auth", phoneNumber);
		}

		// 2. create user from request
		User user = new User(
			signUpRequest.getEmail(),
			signUpRequest.getPassword()
		);

		// 3. save user
		User savedUser = userRepository.save(user);

		// 4. save profile
		profileService.unitProfile(savedUser, signUpRequest.getPhoneNumber());

		// 5. convert to response
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
		String phoneNumber = request.getPhoneNumber().getValue();
		String certificationCode = RandomStringUtils.randomNumeric(6);

		// 2. send certification code
		smsMessageService.sendAuthenticationCode(phoneNumber, certificationCode);

		// 3. put it to key-value store
		phoneNumberKeyValueStore.save(
			PhoneNumberKey.fromString(phoneNumber),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
	}

	@Override
	public PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request
	) {
		// 1. check authenticated
		boolean authenticated = phoneNumberKeyValueStore.checkKeyValueMatches(
			PhoneNumberKey.fromString(request.getPhoneNumber().getValue()),
			AuthCodeValue.ofUnauthenticated(request.getCertificationCode())
		);

		// 2. return
		return new PhoneNumberAuthenticationCheckResponse(authenticated);
	}
}
