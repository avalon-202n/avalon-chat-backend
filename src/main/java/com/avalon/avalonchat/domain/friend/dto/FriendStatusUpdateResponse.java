package com.avalon.avalonchat.domain.friend.dto;

import com.avalon.avalonchat.domain.friend.domain.Friend;

import lombok.Getter;

@Getter
public class FriendStatusUpdateResponse {
	private final Friend.FriendStatus status;

	public FriendStatusUpdateResponse(Friend.FriendStatus status) {
		this.status = status;
	}

	public static FriendStatusUpdateResponse ofEntity(Friend friend) {
		return new FriendStatusUpdateResponse(friend.getFriendStatus());
	}
}
