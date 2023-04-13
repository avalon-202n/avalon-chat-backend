package com.avalon.avalonchat.domain.login.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.exception.LoginInvalidInputException;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.service.UserServiceImpl;
import com.avalon.avalonchat.testsupport.DtoFixture;

@SpringBootTest
public class LoginServiceTest {
	@Autowired
	private LoginServiceImpl sut;
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Test
	void 로그인_성공() {
		SignUpRequest request = DtoFixture.signUpRequest("avalon@e.com", "passw0rd");
		userServiceImpl.signUp(request);

		LoginRequest loginRequest = DtoFixture.loginRequest("avalon@e.com", "passw0rd");
		LoginResponse loginResponse = sut.login(loginRequest);

		assertThat(loginResponse.getEmail().getValue()).isEqualTo(loginRequest.getEmail().getValue());
		assertThat(loginResponse.getToken()).isNotNull();
	}

	@Test
	void 로그인_실패() {
		SignUpRequest request = DtoFixture.signUpRequest("test@e.com", "passw0rd");
		userServiceImpl.signUp(request);

		LoginRequest loginRequest = DtoFixture.loginRequest("test@e.com", "wrongPassword");

		// when then
		assertThatExceptionOfType(LoginInvalidInputException.class)
			.isThrownBy(() -> sut.login(loginRequest));
	}
}
