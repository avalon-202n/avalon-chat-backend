package com.avalon.avalonchat.domain.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
}
