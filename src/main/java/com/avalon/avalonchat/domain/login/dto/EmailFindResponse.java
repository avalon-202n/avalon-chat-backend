package com.avalon.avalonchat.domain.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;

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
