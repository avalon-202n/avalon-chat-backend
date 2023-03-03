package com.avalon.avalonchat.domain.user.dto;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.Data;

@Data
public class SignUpResponse {

	private final Email email;

	public static SignUpResponse ofEntity(User user) {
		return new SignUpResponse(user.getEmail());
	}
}
