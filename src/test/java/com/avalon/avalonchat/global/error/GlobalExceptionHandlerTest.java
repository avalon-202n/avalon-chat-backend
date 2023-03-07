package com.avalon.avalonchat.global.error;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

// TODO - use BaseControllerTest when merge done
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(GlobalExceptionHandler.class)
@Import(GlobalExceptionHandlerTest.GenerateExceptionController.class)
class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void badRequest() throws Exception {
		mockMvc.perform(get("/400-badRequest")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpectAll(
				jsonPath("$.code").value(400),
				jsonPath("$.type").value("AvalonChatRuntimeException"),
				jsonPath("$.message").value("")
			);
	}

	@Test
	void notFound() throws Exception {
		mockMvc.perform(get("/404-notFound")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpectAll(
				jsonPath("$.code").value(404),
				jsonPath("$.type").value("NoHandlerFoundException"),
				jsonPath("$.message").value("No handler found for GET /404-notFound")
			);
	}

	@Test
	void internalServerError() throws Exception {
		mockMvc.perform(get("/500-internalServerError")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isInternalServerError())
			.andExpectAll(
				jsonPath("$.code").value(500),
				jsonPath("$.type").value("Exception"),
				jsonPath("$.message").value("")
			);
	}

	@RestController
	static class GenerateExceptionController {

		@GetMapping("/400-badRequest")
		public void badRequest() {
			throw new AvalonChatRuntimeException();
		}

		@GetMapping("/500-internalServerError")
		public void internalServerError() throws Exception {
			throw new Exception();
		}
	}
}
