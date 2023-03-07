package com.avalon.avalonchat.domain.login.dto;

import lombok.Data;

@Data
public class LoginRequest {

	private final String email;
	private final String password;
}
