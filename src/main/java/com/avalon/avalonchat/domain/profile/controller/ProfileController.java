package com.avalon.avalonchat.domain.profile.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.model.SecurityUser;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileSummaryGetRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileSummaryGetResponse;
import com.avalon.avalonchat.domain.profile.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profiles")
public class ProfileController {

	private final ProfileService service;

	@Operation(
		summary = "프로필 생성",
		description = "헤더의 user_id 를 사용하여 user 레코드와 매핑된 profile 레코드가 생성됩니다."
	)
	@PostMapping
	public ResponseEntity<ProfileAddResponse> addProfile(
		@RequestHeader("user-id") Long userId,
		@RequestBody ProfileAddRequest request
	) {
		ProfileAddResponse response = service.addProfile(userId, request);
		return created(response);
	}

	@Operation(
		summary = "내 프로필 조회",
		description = "내 프로필 상세 조회",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@GetMapping("/me")
	public ProfileDetailedGetResponse getMyProfile(
		@AuthenticationPrincipal SecurityUser securityUser
	) {
		return service.getDetailedById(securityUser.getProfileId());
	}

	@Operation(
		summary = "친구 프로필 목록 조회",
		description = "인증객체의 profileId 와 검색어 nickname 을 사용해 친구 프로필을 전체 조회합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@GetMapping
	public List<ProfileSummaryGetResponse> getFriendProfiles(
		@AuthenticationPrincipal SecurityUser securityUser,
		ProfileSummaryGetRequest request
	) {
		return service.getSummaryById(securityUser.getProfileId(), request);
	}
}
