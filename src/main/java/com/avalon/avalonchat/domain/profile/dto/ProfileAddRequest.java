package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.Getter;

@Getter
public class ProfileAddRequest {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;
	private ProfileImageAddRequest profileImage;
	private BackgroundImageAddRequest backgroundImage;

	public ProfileAddRequest(
		LocalDate birthDate,
		String nickname,
		String bio,
		String profileImage,
		String backgroundImage) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		setProfileImage(profileImage);
		setBackgroundImage(backgroundImage);
	}

	public void setProfileImage(String profileImageUrl) {
		this.profileImage = new ProfileImageAddRequest(profileImageUrl);
	}

	public void setBackgroundImage(String backgroundImageUrl) {
		this.backgroundImage = new BackgroundImageAddRequest(backgroundImageUrl);
	}

	public Profile toEntity(User user) {
		return new Profile(user, this.bio, this.birthDate, this.nickname);
	}
}
