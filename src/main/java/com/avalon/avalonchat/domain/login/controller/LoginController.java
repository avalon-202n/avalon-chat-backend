package com.avalon.avalonchat.domain.login.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "login", description = "로그인과 관련된 endpoint 모음")
@RequestMapping("/login")
@RestController
public class LoginController {

	private final LoginService loginService;

	@Operation(summary = "로그인")
	@PostMapping
	public LoginResponse login(
		@RequestBody LoginRequest request
	) {
		return loginService.login(request);
	}
}
