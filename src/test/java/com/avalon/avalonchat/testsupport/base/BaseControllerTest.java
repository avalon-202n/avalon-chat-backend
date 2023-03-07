package com.avalon.avalonchat.testsupport.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import com.avalon.avalonchat.global.configuration.JacksonConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@MockBean(JpaMetamodelMappingContext.class)
@Import(JacksonConfiguration.class)
public abstract class BaseControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	protected String toJson(Object dto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(dto);
	}
}
