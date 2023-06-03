package com.avalon.avalonchat.controller;

import static com.avalon.avalonchat.global.util.ResponseEntityUtil.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avalon.avalonchat.core.friend.application.FriendService;
import com.avalon.avalonchat.core.friend.dto.FriendEmailAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendEmailAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateResponse;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeRequest;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeResponse;
import com.avalon.avalonchat.global.model.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "친구 API", description = "WIP")
@RequestMapping("/friends")
@RestController
public class FriendController {
	private final FriendService friendService;

	@Operation(
		summary = "연락처 친구 추가",
		description = "phoneNumber 를 이용하여 friend 레코드를 추가합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@PostMapping("/phoneNumber")
	public ResponseEntity<FriendPhoneNumberAddResponse> addFriendByPhoneNumber(
		@AuthenticationPrincipal SecurityUser securityUser,
		FriendPhoneNumberAddRequest request
	) {
		FriendPhoneNumberAddResponse body = friendService.addFriendByPhoneNumber(securityUser.getProfileId(), request);
		return created(body);
	}

	@Operation(
		summary = "이메일 친구 추가",
		description = "email 을 이용하여 friend 레코드를 추가합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@PostMapping("/email")
	public ResponseEntity<FriendEmailAddResponse> addFriendByEmail(
		@AuthenticationPrincipal SecurityUser securityUser,
		FriendEmailAddRequest request
	) {
		FriendEmailAddResponse body = friendService.addFriendByEmail(securityUser.getProfileId(), request);
		return created(body);
	}

	@Operation(
		summary = "친구 동기화",
		description = "phoneNumbers 를 이용하여 friend 레코드를 추가합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@PostMapping
	public ResponseEntity<Map<String, List<FriendSynchronizeResponse>>> synchronizeFriend(
		@AuthenticationPrincipal SecurityUser securityUser,
		FriendSynchronizeRequest request
	) {
		List<FriendSynchronizeResponse> responses
			= friendService.synchronizeFriend(securityUser.getProfileId(), request);
		Map<String, List<FriendSynchronizeResponse>> body = Map.of("data", responses);
		return created(body);
	}

	@Operation(
		summary = "친구 상태 변경",
		description = "friendProfileId를 사용해 현재 로그인된 회원의 친구의 상태를 변경합니다.",
		security = {@SecurityRequirement(name = "bearer-key")}
	)
	@PatchMapping("/{friendProfileId}")
	public FriendStatusUpdateResponse updateFriendStatus(
		@AuthenticationPrincipal SecurityUser securityUser,
		@PathVariable Long friendProfileId,
		@RequestBody FriendStatusUpdateRequest request
	) {
		return friendService.updateFriendStatus(securityUser.getProfileId(), friendProfileId, request);
	}
}
