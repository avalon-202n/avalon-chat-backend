package com.avalon.avalonchat.domain.profile.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileFindRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileFindResponse;
import com.avalon.avalonchat.domain.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profiles")
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping
	public ResponseEntity<ProfileAddResponse> addProfile(
		//TODO: @AuthenticationPrincipal 활용 -> SecurityUser 타입 Authentication 객체에서 userId 가져올 것
		@RequestBody ProfileAddRequest request
	) {
		// setup
		long userId = 1L;

		// action
		ProfileAddResponse response = profileService.addProfile(userId, request);

		// response
		return created(response);
	}

	@GetMapping("/find")
	public ProfileFindResponse findProfile(
		ProfileFindRequest request
	) {
		return profileService.findProfile(request);
	}
}
