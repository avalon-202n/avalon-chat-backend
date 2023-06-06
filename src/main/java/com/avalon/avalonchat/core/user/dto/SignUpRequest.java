package com.avalon.avalonchat.core.user.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpRequest {

	@NotNull
	private Email email;

	@NotNull
	private Password password;

	@NotNull
	private PhoneNumber phoneNumber;
}
