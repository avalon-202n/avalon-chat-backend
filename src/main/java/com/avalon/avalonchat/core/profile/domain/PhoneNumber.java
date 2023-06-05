package com.avalon.avalonchat.core.profile.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static lombok.AccessLevel.*;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
@Embeddable
public class PhoneNumber {

	private static final Pattern PHONENUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{3,4}-\\d{4}$");

	@Column(name = "phoneNumber", nullable = false)
	private String value;

	private PhoneNumber(String value) {
		checkPatternMatches(PHONENUMBER_PATTERN, value);
		this.value = value.replaceAll("-", "").trim();
	}

	public static PhoneNumber of(String value) {
		return new PhoneNumber(value);
	}

	@Override
	public String toString() {
		return "phoneNumber : " + this.value;
	}

}
