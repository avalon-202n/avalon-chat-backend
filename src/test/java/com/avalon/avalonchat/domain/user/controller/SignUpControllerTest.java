package com.avalon.avalonchat.domain.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.service.UserService;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.base.BaseControllerTest;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class SignUpControllerTest extends BaseControllerTest {

	@MockBean
	private UserService service;

	@Test
	void 회원가입_성공() throws Exception {
		//given
		SignUpRequest request = DtoFixture.signUpRequest("hello@wolrd.com", "passw0rd");
		SignUpResponse response = DtoFixture.expectedResponseOf(request);
		when(service.signUp(request)).thenReturn(response);

		//when
		ResultActions perform = mockMvc.perform(post("/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(request))
		);
		//then
		perform
			.andExpect(status().isOk())
			.andExpectAll(jsonPath("$.email").value("hello@wolrd.com"));
	}

	@Disabled("서비스 계층을 연결하게 되면 사용")
	@ValueSource(booleans = {true, false})
	@ParameterizedTest
	void 이메일_중복_검사_성공(boolean duplicated) throws Exception {
		//given
		EmailDuplicatedCheckRequest request = DtoFixture.emailDuplicatedCheckRequest("hello@world.com");
		EmailDuplicatedCheckResponse response = DtoFixture.emailDuplicatedCheckResponse(duplicated);
		//TODO - when(service.emailDuplicatedCheck(request)).thenReturn(response);

		//when
		ResultActions perform = mockMvc.perform(post("/signup/email/duplicated")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(request))
		);

		//then
		//TODO - verify(service, times(1)).emailDuplicatedCheck(request);
		perform
			.andExpect(status().isOk())
			.andExpectAll(jsonPath("$.duplicated").value(duplicated));
	}

	@Test
	void 이메일_인증번호_발송() throws Exception {
		//given
		EmailAuthenticationSendRequest request = DtoFixture.emailAuthenticationSendRequest("hello@world.com");

		//when
		ResultActions perform = mockMvc.perform(post("/signup/email/authenticate/send")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(request))
		);

		//then
		//TODO - verify(service, times(1)).xxx()
		perform
			.andExpect(status().isNoContent());
	}

	@Disabled("서비스 계층을 연결하게 되면 사용")
	@ValueSource(booleans = {true, false})
	@ParameterizedTest
	void 이메일_인증번호_확인(boolean authenticated) throws Exception {
		//given
		EmailAuthenticationCheckRequest request = DtoFixture.emailAuthenticationCheckRequest("hello@world.com", "cOdE");
		EmailAuthenticationCheckResponse response = DtoFixture.emailAuthenticationCheckResponse(authenticated);
		//TODO - when(service.emailAuthenticationCheck(request)).thenReturn(response);

		//when
		ResultActions perform = mockMvc.perform(post("/signup/email/authenticate/check")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(request))
		);

		//then
		//TODO - verify(service, times(1)).emailAuthenticationCheck(request);
		perform
			.andExpect(status().isOk())
			.andExpectAll(jsonPath("$.authenticated").value(authenticated));
	}

	@Test
	void 핸드폰_인증번호_발송() throws Exception {
		//given
		EmailAuthenticationSendRequest request = DtoFixture.emailAuthenticationSendRequest("hello@world.com");

		//when
		ResultActions perform = mockMvc.perform(post("/signup/email/authenticate/send")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(request))
		);

		//then
		//TODO - verify(service, times(1)).emailAuthenticationSend()
		perform
			.andExpect(status().isNoContent());
	}

	@Disabled("서비스 계층을 연결하게 되면 사용")
	@ValueSource(booleans = {true, false})
	@ParameterizedTest
	void 핸드폰_인증번호_확인(boolean authenticated) throws Exception {
		//given
		PhoneNumberAuthenticationCheckRequest request =
			DtoFixture.phoneNumberAuthenticationCheckRequest("010-1234-5678", "cOdE");
		PhoneNumberAuthenticationCheckResponse response =
			DtoFixture.phoneNumberAuthenticationCheckResponse(authenticated);
		//TODO - when(service.phoneNumberAuthenticationCheck(request)).thenReturn(response);

		//when
		ResultActions perform = mockMvc.perform(post("/signup/email/authenticate/check")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(request))
		);

		//then
		//TODO - verify(service, times(1)).phoneNumberAuthenticationCheck(request);
		perform
			.andExpect(status().isOk())
			.andExpectAll(jsonPath("$.authenticated").value(authenticated));
	}
}
