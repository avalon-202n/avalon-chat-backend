package com.avalon.avalonchat.domain.login.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.mock;

import com.avalon.avalonchat.domain.login.exception.LoginInvalidInputException;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.service.UserServiceImpl;
import com.avalon.avalonchat.testsupport.DtoFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenService;
import com.avalon.avalonchat.testsupport.Fixture;

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

		Assertions.assertThrows(LoginInvalidInputException.class, () -> {
			LoginResponse loginResponse = sut.login(loginRequest);
		});
	}
}
