package com.avalon.avalonchat.domain.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordFindRequest {
	@NotNull
	private Email email;

	@NotNull
	@Schema(description = "이메일 인증 코드", example = "A1B2C3")
	private String certificationCode;
}
