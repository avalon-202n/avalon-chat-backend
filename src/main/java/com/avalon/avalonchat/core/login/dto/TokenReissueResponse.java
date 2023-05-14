package com.avalon.avalonchat.core.login.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenReissueResponse {
	@NotNull
	@Schema(description = "access 인증 토큰", example = "jjj.www.ttt")
	private String accessToken;

	@NotNull
	@Schema(description = "refresh 인증 토큰", example = "jjj.www.ttt")
	private String refreshToken;
}
