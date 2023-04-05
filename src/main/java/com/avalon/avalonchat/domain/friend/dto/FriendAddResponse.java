package com.avalon.avalonchat.domain.friend.dto;

import java.util.ArrayList;
import java.util.List;

import com.avalon.avalonchat.domain.friend.domain.Friend;

import lombok.Getter;

@Getter
public class FriendAddResponse {
	private List<Long> friendIds = new ArrayList<>();

	public FriendAddResponse(List<Friend> friends) {
		for (Friend friend : friends) {
			this.friendIds.add(friend.getId());
		}
	}
}
