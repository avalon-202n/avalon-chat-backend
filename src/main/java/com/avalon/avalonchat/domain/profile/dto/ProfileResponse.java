package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.dto.image.ImageResponse;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileResponse {
	private LocalDate birthDate;
	private String nickname;
	private String bio;
	private ImageResponse image;
	private ImageResponse backgroundImage;

	public ProfileResponse(
		LocalDate birthDate,
		String nickname,
		String bio,
		ProfileImage image,
		ProfileImage backgroundImage) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		setImage(image);
		setBackgroundImage(backgroundImage);
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

	public void setImage(ProfileImage image) {
		this.image = ImageResponse.ofEntity(image);
	}

	public void setBackgroundImage(ProfileImage backgroundImage) {
		this.backgroundImage = ImageResponse.ofEntity(backgroundImage);
	}
}
