package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class PhoneNumberAuthenticationCheckResponse {

	@NotNull
	@Schema(description = "핸드폰 인증 성공 여부")
	private boolean authenticated;
}
