package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class PhoneNumberAuthenticationCheckRequest {

	@NotNull
	private String phoneNumber;

	@NotNull
	private String certificationCode;
}
