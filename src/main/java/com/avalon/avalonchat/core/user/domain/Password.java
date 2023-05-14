package com.avalon.avalonchat.core.user.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Password {
	public static final int MIN_LENGTH = 7;
	public static final int MAX_LENGTH = 16;

	@Setter
	private static Encoder encoder;

	@Column(name = "password", nullable = false)
	private String value;

	private Password(String value) {
		checkLength(MIN_LENGTH, MAX_LENGTH, value, "password");

		this.value = value;
	}

	public static Password of(String value) {
		return new Password(value);
	}

	public boolean matches(Password encodedPassword) {
		return encoder.matches(this.value, encodedPassword.value);
	}

	@PrePersist
	private void encodePassword() {
		this.value = encoder.encode(this.value);
	}

	public interface Encoder {
		String encode(String rawPassword);

		boolean matches(String rawPassword, String encodedPassword);
	}
}
