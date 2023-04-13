package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.Getter;

@Getter
public class ProfileAddResponse {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;
	private final String[] profileImages;
	private final String[] backgroundImages;

	public ProfileAddResponse(
		LocalDate birthDate,
		String nickname,
		String bio,
		String[] profileImages,
		String[] backgroundImages) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		this.profileImages = profileImages;
		this.backgroundImages = backgroundImages;
	}

	public static ProfileAddResponse ofEntity(Profile profile) {
		return new ProfileAddResponse(
			profile.getBirthDate(),
			profile.getNickname(),
			profile.getBio(),
			profile.getProfileImages().stream()
				.map(ProfileImage::getUrl)
				.toArray(String[]::new),
			profile.getBackgroundImages().stream()
				.map(BackgroundImage::getUrl)
				.toArray(String[]::new)
		);
	}
}
