package com.avalon.avalonchat.domain.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumberCode;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckResponse;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendResponse;
import com.avalon.avalonchat.domain.auth.repository.AuthPhoneNumberRepository;
import com.avalon.avalonchat.infra.message.MessageService;
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@SpringBootTest
class AuthServiceImplTest extends BaseTestContainerTest {
	@Autowired
	private AuthService authService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private AuthPhoneNumberRepository repository;

	@Test
	@Disabled
	void 인증코드_전송_성공() {
		// given
		String toPhoneNumber = "01055110625";
		AuthPhoneNumberSendRequest request = new AuthPhoneNumberSendRequest(toPhoneNumber);

		// when
		AuthPhoneNumberSendResponse response = authService.getCode(request);

		// then
		assertThat(response.getPhoneNumber()).isEqualTo(toPhoneNumber);
	}

	@Test
	@Disabled
	void 인증코드_확인_성공() {
		// given
		String toPhoneNumber = "01055110625";
		String code = RandomStringUtils.randomNumeric(6);

		messageService.sendMessage(toPhoneNumber, code);
		AuthPhoneNumberCode authPhoneNumberCode = new AuthPhoneNumberCode(toPhoneNumber, code);
		repository.save(authPhoneNumberCode);

		AuthPhoneNumberCode findAuthPhoneNumberCode = repository.findById(toPhoneNumber).get();
		assertThat(findAuthPhoneNumberCode.isAuthenticated()).isFalse();

		AuthPhoneNumberCheckRequest request = new AuthPhoneNumberCheckRequest(toPhoneNumber, code);

		// when
		AuthPhoneNumberCheckResponse response = authService.compareCode(request);

		// then
		assertThat(response.isAuthenticated()).isTrue();

		AuthPhoneNumberCode updatedAuthPhoneNumberCode = repository.findById(toPhoneNumber).get();
		assertThat(updatedAuthPhoneNumberCode.isAuthenticated()).isTrue();
	}

	@AfterEach
	void tearDown() {
		repository.deleteAll();
	}
}
