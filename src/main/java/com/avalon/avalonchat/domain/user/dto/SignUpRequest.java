package com.avalon.avalonchat.domain.user.dto;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.Data;

@Data
public class SignUpRequest {

	private Email email;
	private Password password;

    public SignUpRequest(Email of, String password) {
    }

    public User toEntity() {
		return new User(
			this.email,
			this.password
		);
	}
}
