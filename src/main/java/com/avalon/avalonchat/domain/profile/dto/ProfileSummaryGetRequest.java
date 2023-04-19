package com.avalon.avalonchat.domain.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProfileSummaryGetRequest {
	@Schema(description = "닉네임", example = "nickName")
	private String nickname;

	public ProfileSummaryGetRequest(String nickname) {
		this.nickname = nickname == null ? "" : nickname;
	}
}
