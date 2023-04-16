package com.avalon.avalonchat.domain.login.controller;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.service.LoginService;
import com.avalon.avalonchat.global.openapi.ErrorResponseApi;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "login", description = "로그인과 관련된 endpoint 모음")
@RequestMapping("/login")
@RestController
public class LoginController {

	private final LoginService loginService;

	@Operation(summary = "로그인")
	@ErrorResponseApi(messages = INVALID_LENGTH, args = {"Password", "max", "min"})
	@PostMapping
	public LoginResponse login(
		@RequestBody LoginRequest request
	) {
		return loginService.login(request);
	}

	@Operation(summary = "비밀번호 찾기")
	@ErrorResponseApi(messages = INVALID_LENGTH, args = {"Password", "max", "min"})
	@GetMapping("/password/find")
	public LoginResponse passwordFind(
		@RequestBody LoginRequest request
	) {
		return loginService.login(request);
	}

	@Operation(summary = "아이디 찾기")
	@ErrorResponseApi(messages = INVALID_LENGTH, args = {"Password", "max", "min"})
	@GetMapping("/email/find")
	public LoginResponse EmailFind(
		@RequestBody LoginRequest request
	) {
		return loginService.login(request);
	}
}
