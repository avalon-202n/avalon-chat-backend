package com.avalon.avalonchat.core.profile.dto;

import com.avalon.avalonchat.core.profile.domain.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileDetailedGetResponse {

	private String bio;
	private String nickname;

	public static ProfileDetailedGetResponse from(Profile profile) {
		return new ProfileDetailedGetResponse(
			profile.getBio(),
			profile.getNickname()
		);
	}
}
