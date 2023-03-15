package com.avalon.avalonchat.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginResponse {

	private final String email;

	@Schema(description = "인증 토큰", example = "jjj.www.ttt", requiredMode = Schema.RequiredMode.REQUIRED)
	private final String token;
}
