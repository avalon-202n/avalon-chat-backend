package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class PhoneNumberAuthenticationCheckRequest {

	@NotNull
	private String phoneNumber;

	@NotNull
	@Schema(description = "인증 번호", example = "123456")
	private String certificationCode;
}
