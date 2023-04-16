package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.Getter;

@Getter
public class ProfileAddResponse {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;
	private final List<String> profileImages;
	private final List<String> backgroundImages;
	private final String phoneNumber;

	public ProfileAddResponse(
		LocalDate birthDate,
		String nickname,
		String bio,
		List<String> profileImages,
		List<String> backgroundImages,
		String phoneNumber) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		this.profileImages = profileImages;
		this.backgroundImages = backgroundImages;
		this.phoneNumber = phoneNumber;
	}

	public static ProfileAddResponse ofEntity(Profile profile) {
		return new ProfileAddResponse(
			profile.getBirthDate(),
			profile.getNickname(),
			profile.getBio(),
			profile.getProfileImages().stream()
				.map(ProfileImage::getUrl)
				.collect(Collectors.toList()),
			profile.getBackgroundImages().stream()
				.map(BackgroundImage::getUrl)
				.collect(Collectors.toList()),
			profile.getPhoneNumber()
		);
	}
}
