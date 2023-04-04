package com.avalon.avalonchat.domain.friend.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.service.FriendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/friends")
public class FriendController {
	private final FriendService friendService;

	@PostMapping
	public ResponseEntity<FriendAddResponse> addFriend(
		//TODO: @AuthenticationPrincipal 활용 -> SecurityUser 타입 Authentication 객체에서 userId 가져올 것
		FriendAddRequest request
	) {
		// setup
		long userId = 1L;

		// action
		FriendAddResponse response = friendService.addFriend(userId, request);

		// response
		return created(response);
	}
}
