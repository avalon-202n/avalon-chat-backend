package com.avalon.avalonchat.core.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

	@NotNull
	private Email email;

	@NotNull
	private Password password;
}
