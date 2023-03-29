package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import lombok.Getter;

@Getter
public class ProfileImageAddRequest {
	private String url;

	public ProfileImageAddRequest(String url) {
		this.url = url;
	}

	public ProfileImage toEntity(Profile profile) {
		return new ProfileImage(profile, this.url);
	}
}
