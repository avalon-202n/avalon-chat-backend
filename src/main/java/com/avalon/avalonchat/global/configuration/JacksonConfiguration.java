package com.avalon.avalonchat.global.configuration;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avalon.avalonchat.domain.user.controller.EmailDeserializer;
import com.avalon.avalonchat.domain.user.controller.EmailSerializer;
import com.avalon.avalonchat.domain.user.domain.Email;

@Configuration(proxyBeanMethods = false)
public class JacksonConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> builder
			.serializerByType(Email.class, new EmailSerializer())
			.deserializerByType(Email.class, new EmailDeserializer());
	}
}
