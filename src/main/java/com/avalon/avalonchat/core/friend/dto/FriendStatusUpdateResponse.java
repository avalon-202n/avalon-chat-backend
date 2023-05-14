package com.avalon.avalonchat.core.friend.dto;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.domain.Friend.Status;

import lombok.Getter;

@Getter
public class FriendStatusUpdateResponse {
	private final Status status;

	public FriendStatusUpdateResponse(Status status) {
		this.status = status;
	}

	public static FriendStatusUpdateResponse ofEntity(Friend friend) {
		return new FriendStatusUpdateResponse(friend.getStatus());
	}
}
