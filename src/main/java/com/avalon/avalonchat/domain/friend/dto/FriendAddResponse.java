package com.avalon.avalonchat.domain.friend.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

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
	private List<String> profileImages;
	private List<String> backgroundImages;
	private Friend.Status status;

	// TODO - resolve n+1 problem,
	// Q - do we really need all image links for added friends?
	public static FriendAddResponse from(Friend friend) {
		return new FriendAddResponse(
			friend.getFriendProfile().getId(),
			friend.getFriendProfile().getNickname(),
			friend.getFriendProfile().getBio(),
			friend.getFriendProfile().getProfileImages().stream()
				.map(ProfileImage::getUrl)
				.collect(Collectors.toList()),
			friend.getFriendProfile().getBackgroundImages().stream()
				.map(BackgroundImage::getUrl)
				.collect(Collectors.toList()),
			friend.getStatus()
		);
	}
}
