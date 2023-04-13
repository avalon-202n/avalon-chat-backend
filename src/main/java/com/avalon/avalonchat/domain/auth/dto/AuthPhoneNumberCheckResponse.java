package com.avalon.avalonchat.domain.auth.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumberCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthPhoneNumberCheckResponse {
	@NotNull
	@Schema(description = "핸드폰 인증 성공 여부")
	private final boolean authenticated;

	public AuthPhoneNumberCheckResponse(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public static AuthPhoneNumberCheckResponse ofEntity(AuthPhoneNumberCode authPhoneNumberCode) {
		return new AuthPhoneNumberCheckResponse(authPhoneNumberCode.isAuthenticated());
	}
}
