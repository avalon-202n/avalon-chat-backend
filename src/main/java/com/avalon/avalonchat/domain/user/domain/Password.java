package com.avalon.avalonchat.domain.user.domain;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;
import static com.avalon.avalonchat.global.util.Preconditions.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
@Embeddable
public class Password {
	private static final int MAXIMUM_LENGTH = 16;
	private static final int MINIMUM_LENGTH = 7;

	@Column(name = "password", nullable = false)
	private String value;

	private Password(String value) {
		checkLength(MAXIMUM_LENGTH, MINIMUM_LENGTH, value,
			INVALID_LENGTH.getMessage("패스워드", MAXIMUM_LENGTH, MINIMUM_LENGTH));
		this.value = value;
	}

	public static Password of(String value) {
		return new Password(value);
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.value = encryptedPassword;
	}
}
