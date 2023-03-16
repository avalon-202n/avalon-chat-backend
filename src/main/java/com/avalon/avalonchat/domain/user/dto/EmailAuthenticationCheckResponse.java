package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailAuthenticationCheckResponse {

	@NotNull
	@Schema(description = "이메일 인증 성공 여부")
	private final boolean authenticated;
}
