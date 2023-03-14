package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileAddResponse {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;

	public ProfileAddResponse(
		LocalDate birthDate,
		String nickname,
		String bio) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
	}

	public static ProfileAddResponse ofEntity(Profile profile) {
		return new ProfileAddResponse(
			profile.getBirthDate(),
			profile.getNickname(),
			profile.getBio()
		);
	}
}
