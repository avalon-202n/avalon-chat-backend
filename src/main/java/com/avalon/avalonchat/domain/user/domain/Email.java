package com.avalon.avalonchat.domain.user.domain;

import static com.avalon.avalonchat.global.error.Preconditions.*;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

	/* EMAIL_PATTERN - RFC 5322 */
	private static final Pattern EMAIL_PATTERN =
		Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

	@Column(name = "email", unique = true, nullable = false)
	private String value;

	public static Email of(String value) {
		return new Email(value);
	}

	private Email(String value) {
		checkPatternMatches(EMAIL_PATTERN, value);
		this.value = value;
	}
}
