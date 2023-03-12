package com.avalon.avalonchat.global.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.headers().disable()
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeRequests(authorize -> authorize
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/signup").permitAll()
				.anyRequest().authenticated()
			)
			.build();
	}
}
