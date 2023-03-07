package com.avalon.avalonchat.domain.user.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.Fixture;

class UserServiceImplTest {

	private UserServiceImpl sut;
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository = mock(UserRepository.class);
		sut = new UserServiceImpl(userRepository);
	}

	@DisplayName("회원가입 성공")
	@Test
	void name() {
		//given
		SignUpRequest request = DtoFixture.signUpRequest("hello@wolrd.com", "passw0rd");
		User savedUser = Fixture.createUser("hello@wolrd.com", "passw0rd");
		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		//when
		SignUpResponse response = sut.signUp(request);

		//then
		assertThat(response.getEmail()).isEqualTo(savedUser.getEmail());
	}
}
