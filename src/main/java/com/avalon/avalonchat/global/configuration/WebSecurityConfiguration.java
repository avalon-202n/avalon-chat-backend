package com.avalon.avalonchat.global.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.avalon.avalonchat.global.configuration.jwt.JwtAuthenticationFilter;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfiguration {

	private final JwtTokenService jwtTokenService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).build();
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
				.antMatchers("/signup/**", "/login").permitAll()
				.anyRequest().authenticated()
			)
			.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint)
			).build();
	}

	@Bean
	public JwtAuthenticationFilter tempJwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		RequestMatcher matcher = jwtFilterMatcher(
			"/actuator/**",
			"/**/swagger*/**", "/**/api-docs/**",
			"/signup/**", "/login"
		);

		return new JwtAuthenticationFilter(jwtTokenService, matcher, authenticationManager);
	}

	private RequestMatcher jwtFilterMatcher(String... permitAllAntPatterns) {
		List<RequestMatcher> permitAllMatchers = Arrays.stream(permitAllAntPatterns)
			.map(AntPathRequestMatcher::new)
			.collect(Collectors.toList());

		RequestMatcher permitAllMatcher = new OrRequestMatcher(permitAllMatchers);

		// if not matches permitAll -> require authentication
		return new NegatedRequestMatcher(permitAllMatcher);
	}
}
