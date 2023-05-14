package com.avalon.avalonchat.configuration.jwt;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avalon.avalonchat.global.model.SecurityUser;
import com.avalon.avalonchat.infra.jwt.JwtTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenService jwtTokenService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);

			Jwt unauthenticatedJwt = new Jwt(token);
			Jwt authenticatedJwt = authenticate(unauthenticatedJwt);

			log.debug("authenticate user ");
			SecurityContextHolder.getContext().setAuthentication(authenticatedJwt);
		}
		filterChain.doFilter(request, response);
	}

	private Jwt authenticate(Jwt jwt) throws AuthenticationException {
		Map<String, Object> claims = jwtTokenService.parseClaim((String)jwt.getPrincipal());
		long userId = ((Integer)claims.get("userId")).longValue();
		long profileId = ((Integer)claims.get("profileId")).longValue();

		SecurityUser securityUser = new SecurityUser(userId, profileId);
		return new Jwt(securityUser);
	}
}
