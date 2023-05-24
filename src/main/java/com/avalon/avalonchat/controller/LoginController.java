package com.avalon.avalonchat.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.core.login.application.LoginService;
import com.avalon.avalonchat.core.login.dto.EmailFindResponse;
import com.avalon.avalonchat.core.login.dto.LoginRequest;
import com.avalon.avalonchat.core.login.dto.LoginResponse;
import com.avalon.avalonchat.core.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.core.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.core.login.dto.TokenReissueRequest;
import com.avalon.avalonchat.core.login.dto.TokenReissueResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;

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

	@Operation(summary = "이메일 찾기")
	@GetMapping("/email")
	public EmailFindResponse findEmail(
		@RequestParam(value = "phone_number") String phoneNumber
	) {
		return loginService.findEmailByPhoneNumber(phoneNumber);
	}

	@Operation(summary = "이메일 찾기 핸드폰 인증번호 발송")
	@PostMapping("/email/phonenumber/authenticate/send")
	public ResponseEntity<Void> phoneNumberAuthenticationSendForFindingEmail(
		@RequestBody PhoneNumberAuthenticationSendRequest request
	) {
		loginService.sendFindEmailPhoneNumberAuthentication(request);
		return noContent();
	}

	@Operation(summary = "이메일 찾기 핸드폰 인증번호 확인")
	@PostMapping("/email/phonenumber/authenticate/check")
	public PhoneNumberAuthenticationCheckResponse phoneNumberAuthenticationCheckForFindingEmail(
		@RequestBody PhoneNumberAuthenticationCheckRequest request
	) {
		return loginService.checkFindEmailPhoneNumberAuthentication(request);
	}
}
