package com.avalon.avalonchat.domain.profile.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProfileFindRequest {

	@NotNull
	@Schema(description = "핸드폰 번호", example = "010-1234-5678")
	private final String phoneNumber;

	public ProfileFindRequest(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
