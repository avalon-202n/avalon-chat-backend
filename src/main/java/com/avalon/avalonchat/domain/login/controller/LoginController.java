package com.avalon.avalonchat.domain.login.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LoginController {

	private final LoginService loginService;

	@Operation(summary = "로그인")
	@PostMapping("/login")
	public LoginResponse login(
		@RequestBody LoginRequest request
	) {
		return loginService.login(request);
	}
}
