package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpRequest {

	@NotNull
	private Email email;

	@NotNull
	@Schema(description = "비밀번호", example = "password")
	private Password password;

	public User toEntity() {
		return new User(
			this.email,
			this.password
		);
	}
}
