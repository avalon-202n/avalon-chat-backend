package com.avalon.avalonchat.core.user.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import com.avalon.avalonchat.global.error.exception.BadRequestException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.core.user.dto.SignUpResponse;
import com.avalon.avalonchat.testsupport.DtoFixture;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserServiceImplTest {

	@Autowired
	private UserServiceImpl sut;

	@Autowired
	private PhoneNumberAuthCodeStore phoneNumberAuthKeyValueStore;

	@Test
	@Transactional
	void 회원가입_성공() {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "010-4880-7012";
		PhoneNumber phoneNumber = PhoneNumber.of(toPhoneNumber);

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
		PhoneNumberAuthenticationCheckResponse phoneNumberAuthenticationCheckResponse = sut.checkPhoneNumberAuthentication(
			new PhoneNumberAuthenticationCheckRequest(phoneNumber, certificationCode)
		);

		SignUpRequest request = DtoFixture.signUpRequest("hello1@wolrd.com", "passw0rd", toPhoneNumber);

		//when
		SignUpResponse response = sut.signUp(request);

		//then
		assertThat(phoneNumberAuthenticationCheckResponse.isAuthenticated()).isTrue();
		assertThat(response.getEmail()).isEqualTo(request.getEmail());
	}

	@Test
	@Transactional
	void 회원가입_핸드폰인증되지않은사용자_예외던지기_성공() {
		// given
		SignUpRequest request = DtoFixture.signUpRequest("hello2@wolrd.com", "passw0rd", "010-2222-2222");

		//then & when
		assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> sut.signUp(request));
	}

	@Test
	@Transactional
	void 이메일_중복_확인_성공() {
		// given - authenticate phone number
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "010-5511-0625";
		PhoneNumber phoneNumber = PhoneNumber.of(toPhoneNumber);

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
		sut.checkPhoneNumberAuthentication(
			new PhoneNumberAuthenticationCheckRequest(phoneNumber, certificationCode)
		);

		SignUpRequest request = DtoFixture.signUpRequest("savedUser@wolrd.com", "passw0rd", toPhoneNumber);
		sut.signUp(request);

		//when
		EmailDuplicatedCheckResponse trueResponse =
			sut.checkEmailDuplicated(new EmailDuplicatedCheckRequest(request.getEmail()));
		EmailDuplicatedCheckResponse falseResponse =
			sut.checkEmailDuplicated(new EmailDuplicatedCheckRequest(Email.of("unsavedUser@world.com")));

		//then
		assertThat(trueResponse.isDuplicated()).isTrue();
		assertThat(falseResponse.isDuplicated()).isFalse();
	}

	@Test
	@Disabled
	void 폰번호_인증번호_전송_성공() {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String wrongCertificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "010-5511-0625";
		PhoneNumber phoneNumber = PhoneNumber.of(toPhoneNumber);

		//PhoneNumberAuthenticationSendRequest request = new PhoneNumberAuthenticationSendRequest(phoneNumber);

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
		PhoneNumberAuthenticationCheckResponse phoneNumberAuthenticationCheckResponse = sut.checkPhoneNumberAuthentication(
			new PhoneNumberAuthenticationCheckRequest(phoneNumber, wrongCertificationCode)
		);

		// when
		//sut.sendPhoneNumberAuthentication(request);
		//boolean authenticated = phoneNumberAuthKeyValueStore.isAuthenticated(
		//	PhoneNumberKey.fromString(phoneNumber.getValue()));

		// then
		assertThat(phoneNumberAuthenticationCheckResponse.isAuthenticated()).isFalse();
	}

	@Test
	void 폰번호_인증_성공() {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "010-5511-0625";
		PhoneNumber phoneNumber = PhoneNumber.of(toPhoneNumber);

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);

		PhoneNumberAuthenticationCheckRequest request
			= new PhoneNumberAuthenticationCheckRequest(phoneNumber, certificationCode);

		// when
		PhoneNumberAuthenticationCheckResponse response = sut.checkPhoneNumberAuthentication(request);
		boolean authenticated = phoneNumberAuthKeyValueStore.isAuthenticated(PhoneNumberKey.fromString(phoneNumber.getValue()));
		System.out.println(PhoneNumberKey.fromString(phoneNumber.getValue()));
		// then
		assertThat(response.isAuthenticated()).isTrue();
		assertThat(authenticated).isTrue();
	}
}
