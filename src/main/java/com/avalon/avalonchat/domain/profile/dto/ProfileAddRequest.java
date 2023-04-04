package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class ProfileAddRequest {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;
	private final String profileImage;
	private final String backgroundImage;

	public ProfileAddRequest(
		LocalDate birthDate,
		String nickname,
		String bio,
		String profileImage,
		String backgroundImage
	) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		this.profileImage = profileImage;
		this.backgroundImage = backgroundImage;
	}
}
