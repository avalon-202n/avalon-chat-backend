package com.avalon.avalonchat.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public SignUpResponse signUp(SignUpRequest request) {

		return userService.signUp(request);
	}
}
