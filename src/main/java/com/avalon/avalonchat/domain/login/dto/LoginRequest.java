package com.avalon.avalonchat.domain.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;

import lombok.Data;

@Data
public class LoginRequest {

	@NotNull
	private final Email email;

	@NotNull
	private final String password;
}
