package com.avalon.avalonchat.domain.friend.dto;

import com.avalon.avalonchat.domain.friend.domain.Friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendAddResponse {
	private long friendProfileId;
	private String nickname;
	private String bio;
	private String profileImage;
	private Friend.Status status;
}
