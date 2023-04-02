package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;

import lombok.Getter;

@Getter
public class BackgroundImageAddRequest {
	private String url;

	public BackgroundImageAddRequest(String url) {
		this.url = url;
	}

	public BackgroundImage toEntity(Profile profile) {
		return new BackgroundImage(profile, this.url);
	}
}
