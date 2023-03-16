package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class SignUpResponse {

	@NotNull
	private Email email;

	public static SignUpResponse ofEntity(User user) {
		return new SignUpResponse(user.getEmail());
	}
}
