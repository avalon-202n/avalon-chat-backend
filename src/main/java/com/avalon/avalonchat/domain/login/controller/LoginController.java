package com.avalon.avalonchat.domain.login.controller;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.login.dto.EmailFindRequest;
import com.avalon.avalonchat.domain.login.dto.EmailFindResponse;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.domain.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.domain.login.service.LoginService;
import com.avalon.avalonchat.global.openapi.ErrorResponseApi;

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
	@ErrorResponseApi(messages = INVALID_LENGTH, args = {"Password", "7", "16"})
	@PostMapping
	public LoginResponse login(
		@RequestBody LoginRequest request
	) {
		return loginService.login(request);
	}

	@Operation(summary = "비밀번호 재설정")
	@GetMapping("/password/reset")
	public PasswordFindResponse resetPassword(
		@RequestBody PasswordFindRequest request
	) {
		return loginService.findPasswordByEmail(request);
	}

	@Operation(summary = "아이디 찾기")
	@GetMapping("/email/find")
	public EmailFindResponse findEmail(
		@RequestBody EmailFindRequest request
	) {
		return loginService.findEmailByPhoneNumber(request);
	}
}
