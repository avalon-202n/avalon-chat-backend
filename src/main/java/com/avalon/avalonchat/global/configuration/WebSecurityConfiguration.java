package com.avalon.avalonchat.global.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.avalon.avalonchat.global.configuration.jwt.JwtAuthenticationFilter;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenService;
import com.avalon.avalonchat.global.configuration.security.CustomAuthenticationEntryPoint;
import com.avalon.avalonchat.global.configuration.security.CustomRequestMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfiguration {

	private final JwtTokenService jwtTokenService;

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		AuthenticationManager authenticationManager
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
			.authorizeRequests(authorize -> authorize
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/**/swagger*/**", "/**/api-docs/**").permitAll()
				.antMatchers("/signup/**", "/login").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(tempJwtAuthenticationFilter(authenticationManager),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			).build();
	}

	private JwtAuthenticationFilter tempJwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		List<String> skipPaths = new ArrayList<>();
		skipPaths.add("/login");
		skipPaths.add("/signup");

		final RequestMatcher matcher = new CustomRequestMatcher(skipPaths);
		final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenService, matcher);
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}
}
