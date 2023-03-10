package com.avalon.avalonchat.domain.profile.controller;

import com.avalon.avalonchat.domain.profile.dto.ProfileBioRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileBioResponse;
import com.avalon.avalonchat.domain.profile.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profile")
public class ProfileController {

	private final ProfileService profileService;

	//TODO 프로필 사진 업데이트

	//TODO 프로필 생일 업데이트

	//TODO 프로필 닉네임 업데이트

	//TODO 프로필 연락처 업데이트
	@PostMapping("/bio/{id}")
	public ProfileBioResponse updateBio(@PathVariable Long id, @RequestBody ProfileBioRequest request) {
		return profileService.updateBio(id, request);
	}
}
