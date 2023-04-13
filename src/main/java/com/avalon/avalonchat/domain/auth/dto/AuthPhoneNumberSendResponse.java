package com.avalon.avalonchat.domain.auth.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumberCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthPhoneNumberSendResponse {
	@NotNull
	@Schema(description = "핸드폰 번호", example = "010-1234-5678")
	private final String phoneNumber;

	public AuthPhoneNumberSendResponse(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static AuthPhoneNumberSendResponse ofEntity(AuthPhoneNumberCode authPhoneNumberCode) {
		return new AuthPhoneNumberSendResponse(authPhoneNumberCode.getPhoneNumber());
	}
}
