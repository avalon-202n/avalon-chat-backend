package com.avalon.avalonchat.domain.friend.dto;

import com.avalon.avalonchat.domain.friend.domain.Friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class FriendStatusUpdateRequest {
	@Schema(description = "변경할 친구 상태", example = "NORMAL,FAVORITES,BLOCKED,HIDDEN 중 하나")
	private Friend.FriendStatus status;

	public FriendStatusUpdateRequest(String status) {
		this.status = Friend.FriendStatus.valueOf(status);
	}
}
