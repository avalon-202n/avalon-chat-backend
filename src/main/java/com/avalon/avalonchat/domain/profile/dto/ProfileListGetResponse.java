package com.avalon.avalonchat.domain.profile.dto;

import lombok.Getter;

@Getter
public class ProfileListGetResponse {
	private final String nickname;
	private final String bio;
	private final String profileImageUrl;

	public ProfileListGetResponse(String nickname, String bio, String profileImageUrl) {
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
	}
}
