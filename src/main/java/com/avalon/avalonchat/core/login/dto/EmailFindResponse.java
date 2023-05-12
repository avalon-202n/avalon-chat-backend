package com.avalon.avalonchat.core.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.core.user.domain.Email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailFindResponse {
	@NotNull
	@Schema(description = "이메일", example = "test1234@gmail.com")
	private Email email;
}
