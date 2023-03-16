package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.user.domain.Email;

import lombok.Data;

@Data
public class EmailAuthenticationCheckRequest {

	@NotNull
	private final Email email;
}
