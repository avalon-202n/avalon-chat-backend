package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileUpdateResponse {

	private LocalDate birthDate;
	private String nickname;
	private String bio;
	private String profileImageUrl;
	private List<String> backgroundImageUrls;
	private String phoneNumber;

	public static ProfileUpdateResponse from(Profile profile) {
		return new ProfileUpdateResponse(
			profile.getBirthDate(),
			profile.getNickname(),
			profile.getBio(),
			profile.getLatestProfileImageUrl(),
			profile.getBackgroundImages().stream()
				.map(BackgroundImage::getUrl)
				.collect(Collectors.toList()),
			profile.getPhoneNumber()
		);
	}
}
