package com.avalon.avalonchat.domain.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumber;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberResponse;
import com.avalon.avalonchat.domain.auth.repository.AuthPhoneNumberRepository;
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@SpringBootTest
class AuthServiceImplTest extends BaseTestContainerTest {
	@Autowired
	private AuthService authService;
	@Autowired
	private CoolSmsMessageService messageService;
	@Autowired
	private AuthPhoneNumberRepository repository;

	@Test
	void 인증코드_전송_성공() {
		// given
		String toPhoneNumber = "01055110625";
		AuthPhoneNumberRequest.Get request = new AuthPhoneNumberRequest.Get(toPhoneNumber);

		// when
		AuthPhoneNumberResponse.Get response = authService.getCode(request);

		// then
		assertThat(response.getPhoneNumber()).isEqualTo(toPhoneNumber);
	}

	@Test
	void 인증코드_확인_성공() {
		// given
		String toPhoneNumber = "01055110625";
		StringBuffer code = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			code.append((random.nextInt(10)));
		}

		messageService.sendMessage(toPhoneNumber, code.toString());
		AuthPhoneNumber authPhoneNumber = new AuthPhoneNumber(toPhoneNumber, code.toString());
		repository.save(authPhoneNumber);

		AuthPhoneNumber findAuthPhoneNumber = repository.findById(toPhoneNumber).get();
		assertThat(findAuthPhoneNumber.isValidated()).isFalse();

		AuthPhoneNumberRequest.Compare request = new AuthPhoneNumberRequest.Compare(toPhoneNumber, code.toString());

		// when
		AuthPhoneNumberResponse.Compare response = authService.compareCode(request);

		// then
		assertThat(response.isValidated()).isTrue();

		AuthPhoneNumber updatedAuthPhoneNumber = repository.findById(toPhoneNumber).get();
		assertThat(updatedAuthPhoneNumber.isValidated()).isTrue();
	}

	@AfterEach
	void tearDown() {
		repository.deleteAll();
	}
}
