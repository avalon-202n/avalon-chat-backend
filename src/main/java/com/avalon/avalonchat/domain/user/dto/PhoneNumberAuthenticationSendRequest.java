package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class PhoneNumberAuthenticationSendRequest {

	@NotNull
	private String phoneNumber;
}
