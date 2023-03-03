package com.avalon.avalonchat.domain.user.dto;

import com.avalon.avalonchat.domain.user.domain.Email;

import lombok.Data;

@Data
public class SignUpRequest {

	private final Email email;
	private final String password;
}
