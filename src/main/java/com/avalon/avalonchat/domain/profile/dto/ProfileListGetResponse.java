package com.avalon.avalonchat.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileListGetResponse {
	private String nickname;
	private String bio;
	private String profileImageUrl;
}
