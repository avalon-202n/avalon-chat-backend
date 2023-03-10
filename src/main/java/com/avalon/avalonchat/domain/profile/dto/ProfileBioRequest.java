package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileBioRequest {
	private String bio;

	@Builder
	public Profile toEntity(User user) {
		return Profile.builder()
			.user(user)
			.bio(this.bio)
			.build();
	}
}
