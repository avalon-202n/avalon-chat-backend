package com.avalon.avalonchat.core.user.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpResponse {

	// TODO add response field - userId

	@NotNull
	private Email email;

	public static SignUpResponse ofEntity(User user) {
		return new SignUpResponse(user.getEmail());
	}
}
