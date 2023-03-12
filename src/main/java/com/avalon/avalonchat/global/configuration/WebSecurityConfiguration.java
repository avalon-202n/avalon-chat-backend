package com.avalon.avalonchat.global.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.avalon.avalonchat.domain.user.service.JwtUserDetailsService;
import com.avalon.avalonchat.global.configuration.jwt.JwtAuthenticationFilter;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenProvider;
import com.avalon.avalonchat.global.configuration.security.CustomAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtUserDetailsService jwtUserDetailsService;

	JwtAuthenticationFilter jwtAuthenticationFilter(
		JwtTokenProvider jwtTokenProvider,
		JwtUserDetailsService jwtUserDetailsService
	) {
		return new JwtAuthenticationFilter(jwtTokenProvider, jwtUserDetailsService);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.headers().disable()
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeRequests(authorize -> authorize
				.antMatchers("/signup","/login").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtUserDetailsService),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
		return http.build();
	}
}
