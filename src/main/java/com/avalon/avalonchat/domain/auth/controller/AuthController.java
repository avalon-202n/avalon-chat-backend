package com.avalon.avalonchat.domain.auth.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckResponse;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendResponse;
import com.avalon.avalonchat.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/phone_number")
	public ResponseEntity<AuthPhoneNumberSendResponse> getCode(
		AuthPhoneNumberSendRequest request
	) {
		AuthPhoneNumberSendResponse response = authService.getCode(request);
		return created(response);
	}

	@PatchMapping("/phone_number")
	public AuthPhoneNumberCheckResponse compareCode(
		AuthPhoneNumberCheckRequest request
	) {
		return authService.compareCode(request);
	}
}
