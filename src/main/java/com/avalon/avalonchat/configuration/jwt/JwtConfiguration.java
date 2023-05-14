package com.avalon.avalonchat.configuration.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avalon.avalonchat.infra.jwt.JwtTokenService;

@Configuration(proxyBeanMethods = false)
public class JwtConfiguration {

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenService jwtTokenService) {
		return new JwtAuthenticationFilter(jwtTokenService);
	}

	@Bean
	public JwtTokenService jwtTokenService(JwtProperties properties) {
		return new JwtTokenService(properties);
	}
}
