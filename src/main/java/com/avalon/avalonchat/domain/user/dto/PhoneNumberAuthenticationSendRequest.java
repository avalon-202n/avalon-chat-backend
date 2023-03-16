package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PhoneNumberAuthenticationSendRequest {

	@NotNull
	private final String phoneNumber;
}
