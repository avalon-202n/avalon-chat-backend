package com.avalon.avalonchat.domain.profile.controller;

import com.avalon.avalonchat.domain.profile.dto.ProfileRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileResponse;
import com.avalon.avalonchat.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/profiles")
public class ProfileController {

	private final ProfileService profileService;
	private final String HEADER_AUTHORIZATION = "AUTHORIZATION";
	private final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";

	@PostMapping()
	public ResponseEntity<ProfileResponse> addProfile(
		HttpServletRequest httpRequest,
		@RequestBody ProfileRequest request
	) {
		String accessToken = httpRequest.getHeader(HEADER_AUTHORIZATION).replace(HEADER_AUTHORIZATION_PREFIX, "");

		//TODO jwt 토큰에서 user_id 꺼내기
		long id = 1L;

		return profileService.addProfile(id, request);
	}
}
