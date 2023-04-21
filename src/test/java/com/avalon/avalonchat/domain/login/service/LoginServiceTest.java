package com.avalon.avalonchat.domain.login.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.service.UserServiceImpl;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.testsupport.DtoFixture;

@Transactional
@SpringBootTest
public class LoginServiceTest {
	@Autowired
	private LoginServiceImpl sut;
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Disabled("회원가입 프로세스에 대한 정리 필요")
	@Test
	void 로그인_성공() {
		//given
		SignUpRequest request = DtoFixture.signUpRequest("avalon@e.com", "passw0rd");
		userServiceImpl.signUp(request);

		//when
		LoginRequest loginRequest = DtoFixture.loginRequest("avalon@e.com", "passw0rd");
		LoginResponse loginResponse = sut.login(loginRequest);

		//then
		assertThat(loginResponse.getEmail().getValue()).isEqualTo(loginRequest.getEmail().getValue());
		assertThat(loginResponse.getToken()).isNotNull();
	}

	@CsvSource({
		"wrong@e.com, passw0rd",
		"test@e.com, wrongPassword"
	})
	@ParameterizedTest
	void 잘못된_이메일_혹은_비밀번호를_사용한_로그인_실패(String email, String password) {
		// given
		SignUpRequest request = DtoFixture.signUpRequest("test@e.com", "passw0rd");
		userServiceImpl.signUp(request);

		LoginRequest loginRequest = DtoFixture.loginRequest(email, password);

		// when then
		assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> sut.login(loginRequest));
	}
}
