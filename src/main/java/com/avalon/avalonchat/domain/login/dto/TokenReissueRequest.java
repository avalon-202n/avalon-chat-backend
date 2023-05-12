package com.avalon.avalonchat.domain.login.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenReissueRequest {

	@NotNull
	@Schema(description = "refresh 인증 토큰", example = "jjj.www.ttt")
	private String refreshToken;
}