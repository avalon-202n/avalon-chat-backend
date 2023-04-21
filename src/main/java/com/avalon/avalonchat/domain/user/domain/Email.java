package com.avalon.avalonchat.domain.user.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import java.util.regex.Pattern;

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
public class Email {

	/* EMAIL_PATTERN - RFC 5322 */
	private static final Pattern EMAIL_PATTERN =
		Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

	@Column(name = "email", unique = true, nullable = false)
	private String value;

	private Email(String value) {
		checkPatternMatches(EMAIL_PATTERN, value);
		this.value = value;
	}

	public static Email of(String value) {
		return new Email(value);
	}

	@Override
	public String toString() {
		return '"' + value + '"';
	}
}
