package com.avalon.avalonchat.domain.profile.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profiles")
public class ProfileController {

	private final ProfileService profileService;

	@Operation(
		summary = "프로필 생성",
		description = "헤더의 user_id 를 사용하여 user 레코드와 매핑된 profile 레코드가 생성됩니다."
	)
	@PostMapping
	public ResponseEntity<ProfileAddResponse> addProfile(
		@RequestHeader("user-id") Long userId,
		@RequestBody ProfileAddRequest request
	) {
		// action
		ProfileAddResponse response = profileService.addProfile(userId, request);

		// response
		return created(response);
	}
}
