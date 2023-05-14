package com.avalon.avalonchat.core.user.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static lombok.AccessLevel.*;

import java.util.function.Function;

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
	private static Function<String, String> encodingFunction;

	@Column(name = "password", nullable = false)
	private String value;

	private Password(String value) {
		checkLength(MIN_LENGTH, MAX_LENGTH, value, "password");

		this.value = value;
	}

	public static Password of(String value) {
		return new Password(value);
	}

	@PrePersist
	private void encodePassword() {
		this.value = encodingFunction.apply(this.value);
	}
}
