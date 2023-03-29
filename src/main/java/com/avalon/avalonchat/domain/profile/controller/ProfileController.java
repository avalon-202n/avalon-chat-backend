package com.avalon.avalonchat.domain.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profiles")
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping()
	public ResponseEntity<ProfileAddResponse> addProfile(
		ProfileAddRequest request
	) {
		//TODO: @AuthenticationPrincipal 활용 -> SecurityUser 타입 Authentication 객체에서 userId 가져올 것
		long id = 1L;

		ProfileAddResponse profileAddResponse = profileService.addProfile(id, request);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(profileAddResponse);
	}
}
