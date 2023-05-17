package com.avalon.avalonchat.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.avalon.avalonchat.configuration.jwt.JwtAuthenticationFilter;
import com.avalon.avalonchat.core.user.domain.Password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

	static {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// set encoder as simple delegator of BCryptPasswordEncoder
		Password.setEncoder(
			new Password.Encoder() {
				@Override
				public String encode(String rawPassword) {
					return passwordEncoder.encode(rawPassword);
				}

				@Override
				public boolean matches(String rawPassword, String encodedPassword) {
					return passwordEncoder.matches(rawPassword, encodedPassword);
				}
			}
		);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		JwtAuthenticationFilter jwtFilter,
		AuthenticationEntryPoint authenticationEntryPoint
	) throws Exception {
		return http
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.headers().disable()
			.cors().and()
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests(authorize -> authorize
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/**/swagger*/**", "/**/api-docs/**").permitAll()
				.antMatchers("/signup/**", "/login/**").permitAll()
				.antMatchers("/profiles").permitAll()
				.anyRequest().authenticated()
			)
			.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
			.build();
	}
}
