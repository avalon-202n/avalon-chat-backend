package com.avalon.avalonchat.domain.login.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Password;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PasswordFindResponse {
	@NotNull
	private Password password;
}
