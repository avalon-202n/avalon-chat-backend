package com.avalon.avalonchat.domain.user.controller;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;
import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.EmailAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.service.UserService;
import com.avalon.avalonchat.global.openapi.ErrorResponseApi;

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
	@ErrorResponseApi(SIGNUP_BAD_REQUEST)
	@PostMapping
	public SignUpResponse signUp(
		@RequestBody SignUpRequest request
	) {
		return userService.signUp(request);
	}

	@Operation(summary = "이메일 중복 검사")
	@ErrorResponseApi(messages = INVALID_FIELD, args = {"Email"})
	@PostMapping("/email/duplicated")
	public EmailDuplicatedCheckResponse emailDuplicatedCheck(
		@RequestBody EmailDuplicatedCheckRequest request
	) {
		return new EmailDuplicatedCheckResponse(false);
	}

	@Operation(summary = "이메일 인증번호 발송")
	@ErrorResponseApi(messages = INVALID_FIELD, args = {"Email"})
	@PostMapping("/email/authenticate/send")
	public ResponseEntity<Void> emailAuthenticationSend(
		@RequestBody EmailAuthenticationSendRequest request
	) {
		return noContent();
	}

	@Operation(summary = "이메일 인증번호 확인")
	@ErrorResponseApi(messages = INVALID_FIELD, args = {"Email 인증 번호"})
	@PostMapping("/email/authenticate/check")
	public EmailAuthenticationCheckResponse emailAuthenticationCheck(
		@RequestBody EmailAuthenticationCheckRequest request
	) {
		return new EmailAuthenticationCheckResponse(true);
	}

	@Operation(summary = "핸드폰 인증번호 발송")
	@ErrorResponseApi(messages = INVALID_FIELD, args = {"PhoneNumber"})
	@PostMapping("/phonenumber/authenticate/send")
	public ResponseEntity<Void> phoneNumberAuthenticationSend(
		@RequestBody PhoneNumberAuthenticationSendRequest request
	) {
		return noContent();
	}

	@Operation(summary = "핸드폰 인증번호 확인")
	@ErrorResponseApi(messages = INVALID_FIELD, args = {"PhoneNumber 인증 번호"})
	@PostMapping("/phonenumber/authenticate/check")
	public PhoneNumberAuthenticationCheckResponse phoneNumberAuthenticationCheck(
		@RequestBody PhoneNumberAuthenticationSendRequest request
	) {
		return new PhoneNumberAuthenticationCheckResponse(true);
	}
}