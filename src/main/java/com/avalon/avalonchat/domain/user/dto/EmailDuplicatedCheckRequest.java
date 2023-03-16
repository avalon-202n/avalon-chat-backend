package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailDuplicatedCheckRequest {

	@NotNull
	private final Email email;

	@NotNull
	@Schema(description = "이메일 인증 코드")
	private final String certificationCode;
}
