package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.image.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.image.ProfileImage;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileAddResponse {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;
	private final String profileImageUrl;
	private final String backgroundImageUrl;

	public ProfileAddResponse(
		LocalDate birthDate,
		String nickname,
		String bio,
		String profileImageUrl,
		String backgroundImageUrl) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
		this.backgroundImageUrl = backgroundImageUrl;
	}

	public static ProfileAddResponse ofEntity(Profile profile, ProfileImage profileImage, BackgroundImage backgroundImage) {
		return new ProfileAddResponse(
			profile.getBirthDate(),
			profile.getNickname(),
			profile.getBio(),
			profileImage.getUrl(),
			backgroundImage.getUrl()
		);
	}
}
