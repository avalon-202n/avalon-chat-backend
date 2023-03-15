package com.avalon.avalonchat.domain.user.controller;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
