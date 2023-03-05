package com.avalon.avalonchat.domain.user.dto;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.Data;

@Data
public class SignUpRequest {

	private final Email email;
	private final String password;

	public User toEntity() {
		return new User(
			this.email,
			this.password
		);
	}
}
