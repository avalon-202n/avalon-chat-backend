package com.avalon.avalonchat.domain.user.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Password {
	public static final int MIN_LENGTH = 7;
	public static final int MAX_LENGTH = 16;

	@Column(name = "password", nullable = false)
	private String value;

	private Password(String value) {
		checkLength(MIN_LENGTH, MAX_LENGTH, value, "password");
		this.value = value;
	}

	public static Password of(String value) {
		return new Password(value);
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.value = encryptedPassword;
	}
}
