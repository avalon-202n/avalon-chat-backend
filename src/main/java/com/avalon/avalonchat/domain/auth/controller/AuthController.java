package com.avalon.avalonchat.domain.auth.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberResponse;
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
	public ResponseEntity<AuthPhoneNumberResponse.Get> getCode(
		AuthPhoneNumberRequest.Get request
	) {
		AuthPhoneNumberResponse.Get response = authService.getCode(request);
		return created(response);
	}

	@PatchMapping("/phone_number")
	public AuthPhoneNumberResponse.Compare compareCode(
		AuthPhoneNumberRequest.Compare request
	) {
		return authService.compareCode(request);
	}
}
