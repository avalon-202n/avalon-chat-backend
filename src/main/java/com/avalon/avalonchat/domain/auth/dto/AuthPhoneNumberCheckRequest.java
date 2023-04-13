package com.avalon.avalonchat.domain.auth.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthPhoneNumberCheckRequest {
	@NotNull
	@Schema(description = "핸드폰 번호", example = "010-1234-5678")
	private final String phoneNumber;
	@NotNull
	@Schema(description = "인증 번호", example = "123456")
	private final String code;

	public AuthPhoneNumberCheckRequest(String phoneNumber, String code) {
		this.phoneNumber = phoneNumber;
		this.code = code;
	}
}
