package com.avalon.avalonchat.core.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.User;

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
	@Schema(description = "access 인증 토큰", example = "jjj.www.ttt")
	private String accessToken;

	@NotNull
	@Schema(description = "refresh 인증 토큰", example = "jjj.www.ttt")
	private String refreshToken;

	@NotNull
	@Schema(description = "유저 상태", example = "ACTIVE")
	private User.Status userStatus;
}
