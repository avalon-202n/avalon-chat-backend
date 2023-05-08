package com.avalon.avalonchat.domain.login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.login.dto.EmailFindResponse;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.domain.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.domain.login.dto.TokenReissueRequest;
import com.avalon.avalonchat.domain.login.dto.TokenReissueResponse;
import com.avalon.avalonchat.domain.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "login")
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

	@Operation(summary = "token 재발급")
	@PostMapping("/token/reissue")
	public TokenReissueResponse refreshToken(
		@RequestBody TokenReissueRequest request
	) {
		return loginService.reissueToken(request);
	}

	@Operation(summary = "비밀번호 초기화")
	@PostMapping("/password/reset")
	public PasswordFindResponse resetPassword(
		@RequestBody PasswordFindRequest request
	) {
		return loginService.resetPassword(request);
	}

	@Operation(summary = "아이디 찾기")
	@GetMapping("/email")
	public EmailFindResponse findEmail(
		@RequestParam(value = "phone_number", required = true) String phoneNumber
	) {
		return loginService.findEmailByPhoneNumber(phoneNumber);
	}
}
