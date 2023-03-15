package com.avalon.avalonchat.global.configuration;

import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.global.configuration.openapi.CustomSchemas;
import com.avalon.avalonchat.global.error.ErrorResponse;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration(proxyBeanMethods = false)
public class OpenAPIConfiguration {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("AVALONCHAT API")
			.version("v1.0")
			.description("avalon-chat REST API documentation");

		Components components = new Components();

		return new OpenAPI()
			.info(info)
			.components(components);
	}

	@Bean
	public ApplicationRunner applyCustomConfigurations() {
		return args -> {
			SpringDocUtils.getConfig().replaceWithSchema(Email.class, CustomSchemas.EMAIL_SCHEMA);
			SpringDocUtils.getConfig().addResponseTypeToIgnore(ErrorResponse.class);
		};
	}
}
