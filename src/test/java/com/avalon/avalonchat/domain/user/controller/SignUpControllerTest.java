package com.avalon.avalonchat.domain.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.service.UserService;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.base.BaseControllerTest;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SignUpController.class)
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

	// TODO add service mocking scenario
	@ValueSource(booleans = {true, false})
	@ParameterizedTest
	void 이메일_중복_검사_성공(boolean duplicated) throws Exception {
		//given
		EmailDuplicatedCheckRequest request = DtoFixture.emailDuplicatedCheckRequest("hello@world.com");
		EmailDuplicatedCheckResponse response = DtoFixture.emailDuplicatedCheckResponse(duplicated);
		// when(service.emailDuplicatedCheck(request)).thenReturn(response);

		//when
		ResultActions perform = mockMvc.perform(post("/signup/email/duplicated")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJson(request))
		);

		//then
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
		perform
			.andExpect(status().isNoContent());
	}
}
