package com.avalon.avalonchat.core.user.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailAuthenticationCheckResponse {

	@NotNull
	@Schema(description = "이메일 인증 성공 여부")
	private boolean authenticated;
}
