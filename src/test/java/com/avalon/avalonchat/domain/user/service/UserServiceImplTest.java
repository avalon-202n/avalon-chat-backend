package com.avalon.avalonchat.domain.user.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.testsupport.DtoFixture;

@SpringBootTest
class UserServiceImplTest {

	@Autowired
	private UserServiceImpl sut;

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
}
