package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.Getter;

@Getter
public class ProfileFindResponse {
	private final String nickname;
	private final String bio;
	private final String profileImageUrl;
	private final String backgroundImageUrl;

	public ProfileFindResponse(
		String nickname,
		String bio,
		String profileImageUrl,
		String backgroundImageUrl
	) {
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
		this.backgroundImageUrl = backgroundImageUrl;
	}

	public static ProfileFindResponse ofEntity(
		Profile profile,
		ProfileImage profileImage,
		BackgroundImage backgroundImage
	) {
		return new ProfileFindResponse(
			profile.getNickname(),
			profile.getBio(),
			profileImage.getUrl(),
			backgroundImage.getUrl()
		);
	}
}
