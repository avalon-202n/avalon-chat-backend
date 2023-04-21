package com.avalon.avalonchat.domain.user.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.PhoneNumberAuthenticationCode;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.repository.PhoneNumberAuthenticationRepository;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@SpringBootTest
class UserServiceImplTest extends BaseTestContainerTest {

	@Autowired
	private UserServiceImpl sut;
	@Autowired
	private MessageService messageService;
	@Autowired
	private PhoneNumberAuthenticationRepository phoneNumberAuthenticationRepository;

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

		// when & then
		sut.sendPhoneNumberAuthentication(request);

		PhoneNumberAuthenticationCode saved = phoneNumberAuthenticationRepository.findById(toPhoneNumber).get();
		assertThat(saved.getCertificationCode()).isNotNull();
		assertThat(saved.isAuthenticated()).isFalse();
	}

	@Test
	@Disabled
	void 폰번호_인증_성공() {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "01055110625";

		messageService.sendAuthenticationCode(toPhoneNumber, certificationCode);

		PhoneNumberAuthenticationCode entity = new PhoneNumberAuthenticationCode(toPhoneNumber, certificationCode);
		phoneNumberAuthenticationRepository.save(entity);

		PhoneNumberAuthenticationCheckRequest request = new PhoneNumberAuthenticationCheckRequest(
			toPhoneNumber, certificationCode);

		// when & then
		PhoneNumberAuthenticationCheckResponse response = sut.checkPhoneNumberAuthentication(request);

		assertThat(response.isAuthenticated()).isTrue();

		PhoneNumberAuthenticationCode saved = phoneNumberAuthenticationRepository.findById(toPhoneNumber).get();

		assertThat(saved.getPhoneNumber()).isEqualTo(toPhoneNumber);
		assertThat(saved.getCertificationCode()).isEqualTo(certificationCode);
		assertThat(saved.isAuthenticated()).isTrue();
	}

	@AfterEach
	void tearDown() {
		phoneNumberAuthenticationRepository.deleteAll();
	}
}
