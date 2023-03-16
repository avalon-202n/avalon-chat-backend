package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PhoneNumberAuthenticationCheckRequest {

	@NotNull
	private final String phoneNumber;

	@NotNull
	private final String certificationCode;
}
