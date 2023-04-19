package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.Getter;

@Getter
public class ProfileSummaryGetResponse {
	private final String nickname;
	private final String bio;
	private final String profileImageUrl;

	public ProfileSummaryGetResponse(String nickname, String bio, String profileImageUrl) {
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
	}

	public static ProfileSummaryGetResponse from(Profile profile, ProfileImage profileImage) {
		return new ProfileSummaryGetResponse(profile.getNickname(), profile.getBio(), profileImage.getUrl());
	}
}
