package com.avalon.avalonchat.domain.profile.controller;

import com.avalon.avalonchat.domain.profile.dto.ProfileRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileResponse;
import com.avalon.avalonchat.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profiles")
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping()
	public ResponseEntity<ProfileResponse> addProfile(
		ProfileRequest request
	) {
		//TODO: @AuthenticationPrincipal 활용 -> SecurityUser 타입 Authentication 객체에서 userId 가져올 것
		long id = 1L;

		return profileService.addProfile(id, request);
	}
}
