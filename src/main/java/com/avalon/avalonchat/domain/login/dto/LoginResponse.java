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
public class LoginResponse {

	@NotNull
	private Email email;

	@NotNull
	@Schema(description = "인증 토큰", example = "jjj.www.ttt")
	private String token;
}
