package com.avalon.avalonchat.domain.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

	@NotNull
	private Email email;

	@NotNull
	@Schema(description = "비밀 번호", example = "password")
	private String password;
}
