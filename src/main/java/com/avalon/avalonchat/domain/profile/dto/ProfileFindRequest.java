package com.avalon.avalonchat.domain.profile.dto;

import lombok.Getter;

@Getter
public class ProfileFindRequest {
	private final String phoneNumber;

	public ProfileFindRequest(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
