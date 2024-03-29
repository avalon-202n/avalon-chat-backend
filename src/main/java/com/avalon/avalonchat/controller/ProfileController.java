package com.avalon.avalonchat.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.core.profile.application.ProfileService;
import com.avalon.avalonchat.core.profile.dto.BackgroundImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateResponse;
import com.avalon.avalonchat.global.model.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "프로필 API")
@RequestMapping("/profiles")
@RestController
public class ProfileController {

	private final ProfileService service;

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
		summary = "친구 프로필 상세 조회",
		description = "친구 프로필 아이디로 상세 조회",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@GetMapping("/{profileId}")
	public ProfileDetailedGetResponse getFriendProfile(
		@PathVariable long profileId,
		@AuthenticationPrincipal SecurityUser securityUser
	) {
		return service.getFriendDetailedById(securityUser.getProfileId(), profileId);
	}

	@Operation(
		summary = "친구 프로필 목록 조회",
		description = "인증객체의 profileId 를 사용해 친구 프로필 레코드를 전체 조회합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@GetMapping
	public Map<String, List<ProfileListGetResponse>> getFriendProfiles(
		@AuthenticationPrincipal SecurityUser securityUser
	) {
		List<ProfileListGetResponse> responses = service.getListById(securityUser.getProfileId());
		Map<String, List<ProfileListGetResponse>> body = Map.of("data", responses);
		return body;
	}

	@Operation(
		summary = "프로필 수정",
		description = "인증객체의 profileId에 해당하는 프로필의 데이터를 넘겨받은 요청 dto의 데이터로 수정합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@PatchMapping
	public ProfileUpdateResponse updateProfile(
		@AuthenticationPrincipal SecurityUser securityUser,
		@RequestBody ProfileUpdateRequest request
	) {
		return service.updateProfile(securityUser.getProfileId(), request);
	}

	@Operation(
		summary = "프로필 이미지 삭제",
		description = "프로필 이미지 url을 통해 이미지를 삭제합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@DeleteMapping("/profile_image")
	public ResponseEntity<Void> deleteProfileImage(
		@AuthenticationPrincipal SecurityUser securityUser,
		@RequestBody ProfileImageDeleteRequest request
	) {
		service.deleteProfileImage(securityUser.getProfileId(), request);
		return noContent();
	}

	@Operation(
		summary = "배경 이미지 삭제",
		description = "배경 이미지 url을 통해 이미지를 삭제합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@DeleteMapping("/backgroung_image")
	public ResponseEntity<Void> deleteBackgroundImage(
		@AuthenticationPrincipal SecurityUser securityUser,
		@RequestBody BackgroundImageDeleteRequest request
	) {
		service.deleteBackgroundImage(securityUser.getProfileId(), request);
		return noContent();
	}
}
