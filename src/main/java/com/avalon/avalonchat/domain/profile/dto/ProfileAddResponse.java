package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileAddResponse {
	private LocalDate birthDate;
	private String nickname;
	private String bio;
	private List<String> profileImages;
	private List<String> backgroundImages;
	private String phoneNumber;

	public static ProfileAddResponse from(Profile profile) {
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
