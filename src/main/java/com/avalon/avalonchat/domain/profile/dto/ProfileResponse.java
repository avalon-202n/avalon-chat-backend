package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileResponse {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;
	private final String imagePath;
	private final String backgroundImagePath;

	public ProfileResponse(
		LocalDate birthDate,
		String nickname,
		String bio,
		ProfileImage image,
		ProfileImage backgroundImage) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		this.imagePath = image.getFilePath();
		this.backgroundImagePath = backgroundImage.getFilePath();
	}

	public static ProfileResponse ofEntity(Profile profile, ProfileImage image, ProfileImage backgroundImage) {
		return new ProfileResponse(
			profile.getBirthDate(),
			profile.getNickname(),
			profile.getBio(),
			image,
			backgroundImage
		);
	}
}
