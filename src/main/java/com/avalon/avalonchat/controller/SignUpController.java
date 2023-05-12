package com.avalon.avalonchat.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.core.user.dto.SignUpResponse;
import com.avalon.avalonchat.core.user.application.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "signup", description = "회원가입과 관련된 endpoint 모음")
@RequestMapping("/signup")
@RestController
public class SignUpController {

	private final UserService userService;

	@Operation(
		summary = "회원 가입",
		description = "이메일, 비밀번호를 이용한 회원 가입"
	)
	@PostMapping
	public SignUpResponse signUp(
		@RequestBody SignUpRequest request
	) {
		return userService.signUp(request);
	}

	@Operation(summary = "이메일 중복 검사")
	@PostMapping("/email/duplicated")
	public EmailDuplicatedCheckResponse emailDuplicatedCheck(
		@RequestBody EmailDuplicatedCheckRequest request
	) {
		return userService.checkEmailDuplicated(request);
	}

	@Operation(summary = "핸드폰 인증번호 발송")
	@PostMapping("/phonenumber/authenticate/send")
	public ResponseEntity<Void> phoneNumberAuthenticationSend(
		@RequestBody PhoneNumberAuthenticationSendRequest request
	) {
		userService.sendPhoneNumberAuthentication(request);
		return noContent();
	}

	@Operation(summary = "핸드폰 인증번호 확인")
	@PostMapping("/phonenumber/authenticate/check")
	public PhoneNumberAuthenticationCheckResponse phoneNumberAuthenticationCheck(
		@RequestBody PhoneNumberAuthenticationCheckRequest request
	) {
		return userService.checkPhoneNumberAuthentication(request);
	}
}
