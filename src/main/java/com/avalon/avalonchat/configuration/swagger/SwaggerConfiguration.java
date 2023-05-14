package com.avalon.avalonchat.configuration.swagger;

import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration(proxyBeanMethods = false)
public class SwaggerConfiguration {

	static {
		SpringDocUtils config = SpringDocUtils.getConfig();
		config.replaceWithSchema(Email.class, CustomSchemas.EMAIL_SCHEMA);
		config.replaceWithSchema(Password.class, CustomSchemas.PASSWORD_SCHEMA);
	}

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
}
