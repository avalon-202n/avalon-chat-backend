package com.avalon.avalonchat.domain.friend.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.service.FriendService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/friends")
public class FriendController {
	private final FriendService friendService;

	@Operation(
		summary = "친구 추가",
		description = "phoneNumbers 를 이용하여 friend 레코드를 추가합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@PostMapping
	public ResponseEntity<List<FriendAddResponse>> addFriend(
		//TODO: @AuthenticationPrincipal 활용 -> SecurityUser 타입 Authentication 객체에서 userId 가져올 것
		FriendAddRequest request
	) {
		// setup
		long profileId = 1L;

		// action
		List<FriendAddResponse> responses = friendService.addFriend(profileId, request);

		// response
		return created(responses);
	}
}
