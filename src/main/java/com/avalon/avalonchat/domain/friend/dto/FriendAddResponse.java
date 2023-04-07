package com.avalon.avalonchat.domain.friend.dto;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.Getter;

@Getter
public class FriendAddResponse {
	private final long friendProfileId;
	private final String nickname;
	private final String bio;
	private final String[] profileImages;
	private final String[] backgroundImages;
	private final String friendStatus;

	public FriendAddResponse(Friend friend) {
		this.friendProfileId = friend.getFriendProfile().getId();
		this.nickname = friend.getFriendProfile().getNickname();
		this.bio = friend.getFriendProfile().getBio();
		this.profileImages = friend.getFriendProfile().getProfileImages().stream()
			.map(ProfileImage::getUrl)
			.toArray(String[]::new);
		this.backgroundImages = friend.getFriendProfile().getBackgroundImages().stream()
			.map(BackgroundImage::getUrl)
			.toArray(String[]::new);
		this.friendStatus = friend.getFriendStatus().toString();
	}

	public static FriendAddResponse ofEntity(Friend friend) {
		return new FriendAddResponse(friend);
	}
}
