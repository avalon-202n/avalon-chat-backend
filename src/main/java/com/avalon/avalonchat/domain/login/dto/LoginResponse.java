package com.avalon.avalonchat.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	private final String email;
	private final String accessToken;
}
