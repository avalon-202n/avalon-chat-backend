package com.avalon.avalonchat.global.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration(proxyBeanMethods = false)
public class OpenAPIConfig {

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
}
