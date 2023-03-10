package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileBioResponse {
	private String bio;

	public static ProfileBioResponse ofEntity(Profile profile) {
		return new ProfileBioResponse(profile.getBio());
	}
}
