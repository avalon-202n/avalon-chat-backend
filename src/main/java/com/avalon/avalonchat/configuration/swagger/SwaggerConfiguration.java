package com.avalon.avalonchat.configuration.swagger;

import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avalon.avalonchat.domain.user.domain.Email;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration(proxyBeanMethods = false)
public class SwaggerConfiguration {

	@Bean
	public OpenAPI openApi() {
		Info info = new Info()
			.title("AVALONCHAT API")
			.version("v1.0")
			.description("avalon-chat REST API documentation");

		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT");
		Components components = new Components().addSecuritySchemes("bearer-key", securityScheme);

		return new OpenAPI()
			.info(info)
			.components(components);
	}

	@Bean
	public ApplicationRunner applyCustomConfigurations() {
		return args -> {
			SpringDocUtils.getConfig().replaceWithSchema(Email.class, CustomSchemas.EMAIL_SCHEMA);
		};
	}
}
