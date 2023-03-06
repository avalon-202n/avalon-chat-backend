package com.avalon.avalonchat.domain.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.base.BaseControllerTest;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends BaseControllerTest {

	@MockBean
	private UserController sut;

	@DisplayName("회원가입 성공")
	@Test
	void name() throws Exception {
		//given
		SignUpRequest request = DtoFixture.signUpRequest("hello@wolrd.com", "passw0rd");
		SignUpResponse response = DtoFixture.expectedResponseOf(request);
		when(sut.signUp(request)).thenReturn(response);

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
