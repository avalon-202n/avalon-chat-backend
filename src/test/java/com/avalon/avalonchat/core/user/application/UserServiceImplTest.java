package com.avalon.avalonchat.core.user.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@SpringBootTest
class UserServiceImplTest extends BaseTestContainerTest {

	@Autowired
	private UserServiceImpl sut;
	@Autowired
	private SmsMessageService smsMessageService;

	@Autowired
	private PhoneNumberAuthCodeStore phoneNumberAuthKeyValueStore;

	@Test
	void 회원가입_성공() {
		//given
		SignUpRequest request = DtoFixture.signUpRequest("hello@wolrd.com", "passw0rd");

		//when
		SignUpResponse response = sut.signUp(request);

		//then
		assertThat(response.getEmail()).isEqualTo(request.getEmail());
	}

	@Test
	void 이메일_중복_확인_성공() {
		//given
		SignUpRequest request = DtoFixture.signUpRequest("savedUser@wolrd.com", "passw0rd");
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
		AuthCodeValue authCodeValue = phoneNumberAuthKeyValueStore.get(PhoneNumberKey.fromString(toPhoneNumber));

		// then
		assertThat(authCodeValue.isAuthenticated()).isFalse();
	}

	@Test
	@Disabled
	void 폰번호_인증_성공() {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "01055110625";

		smsMessageService.sendAuthenticationCode(toPhoneNumber, certificationCode);
		phoneNumberAuthKeyValueStore.put(
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
