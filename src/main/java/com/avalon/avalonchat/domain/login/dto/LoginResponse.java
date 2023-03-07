package com.avalon.avalonchat.domain.login.dto;

import lombok.Data;

@Data
public class LoginResponse {

	private final String email;
	private final String token;
}
