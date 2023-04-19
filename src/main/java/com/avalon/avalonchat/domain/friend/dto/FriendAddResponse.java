package com.avalon.avalonchat.domain.friend.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.Getter;

@Getter
public class FriendAddResponse {
	private final long friendProfileId;
	private final String nickname;
	private final String bio;
	private final List<String> profileImages;
	private final List<String> backgroundImages;
	private final Friend.FriendStatus friendStatus;

	public FriendAddResponse(Friend friend) {
		this.friendProfileId = friend.getFriendProfile().getId();
		this.nickname = friend.getFriendProfile().getNickname();
		this.bio = friend.getFriendProfile().getBio();
		this.profileImages = friend.getFriendProfile().getProfileImages().stream()
			.map(ProfileImage::getUrl)
			.collect(Collectors.toList());
		this.backgroundImages = friend.getFriendProfile().getBackgroundImages().stream()
			.map(BackgroundImage::getUrl)
			.collect(Collectors.toList());
		this.friendStatus = friend.getFriendStatus();
	}

	public static FriendAddResponse ofEntity(Friend friend) {
		return new FriendAddResponse(friend);
	}
}
