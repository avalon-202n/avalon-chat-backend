package com.avalon.avalonchat.core.user.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

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

@SpringBootTest
class UserServiceImplTest {

	@Autowired
	private UserServiceImpl sut;

	@Autowired
	private PhoneNumberAuthCodeStore phoneNumberAuthKeyValueStore;

	@Test
	void 회원가입_성공() {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "010-5511-0625";

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(toPhoneNumber),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
		sut.checkPhoneNumberAuthentication(
			new PhoneNumberAuthenticationCheckRequest(toPhoneNumber, certificationCode)
		);

		SignUpRequest request = DtoFixture.signUpRequest("hello@wolrd.com", "passw0rd", toPhoneNumber);

		//when
		SignUpResponse response = sut.signUp(request);

		//then
		assertThat(response.getEmail()).isEqualTo(request.getEmail());
	}

	@Test
	void 이메일_중복_확인_성공() {
		// given - authenticate phone number
		String certificationCode = RandomStringUtils.randomNumeric(6);
		PhoneNumber phoneNumber = PhoneNumber.of("010-5511-0625");

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
		sut.checkPhoneNumberAuthentication(
			new PhoneNumberAuthenticationCheckRequest(phoneNumber.getValue(), certificationCode)
		);

		SignUpRequest request = DtoFixture.signUpRequest("savedUser@wolrd.com", "passw0rd", "010-5511-0625");
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
		String toPhoneNumber = "01055110625";
		PhoneNumberAuthenticationSendRequest request = new PhoneNumberAuthenticationSendRequest(toPhoneNumber);

		// when
		sut.sendPhoneNumberAuthentication(request);
		boolean authenticated = phoneNumberAuthKeyValueStore.isAuthenticated(PhoneNumberKey.fromString(toPhoneNumber));

		// then
		assertThat(authenticated).isFalse();
	}

	@Test
	@Disabled
	void 폰번호_인증_성공() {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "01055110625";

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(toPhoneNumber),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);

		PhoneNumberAuthenticationCheckRequest request
			= new PhoneNumberAuthenticationCheckRequest(toPhoneNumber, certificationCode);

		// when
		PhoneNumberAuthenticationCheckResponse response = sut.checkPhoneNumberAuthentication(request);

		// then
		assertThat(response.isAuthenticated()).isTrue();
	}
}
