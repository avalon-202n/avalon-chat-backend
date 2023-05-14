package com.avalon.avalonchat.core.user.dto;

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
	@Schema(description = "핸드폰 번호", example = "010-1234-5678")
	private String phoneNumber;

	@NotNull
	@Schema(description = "인증 번호", example = "123456")
	private String certificationCode;
}
