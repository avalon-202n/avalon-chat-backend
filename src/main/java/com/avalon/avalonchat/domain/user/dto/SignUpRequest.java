package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.Data;

@Data
public class SignUpRequest {

	@NotNull
	private final Email email;

	@NotNull
	private final String password;

	public User toEntity() {
		return new User(
			this.email,
			this.password
		);
	}
}
