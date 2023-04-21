package com.avalon.avalonchat.global.util;

import java.util.regex.Pattern;

import com.avalon.avalonchat.global.error.exception.InputValidationException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class Preconditions {

	public static void checkNotNull(Object value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
	}

	public static void checkPatternMatches(Pattern pattern, String value) {
		if (!pattern.matcher(value).find()) {
			throw new InputValidationException("pattern", "email");
		}
	}

	public static void checkLength(int minLength, int maxLength, String value, String type) {
		if (value.length() < minLength || value.length() > maxLength) {
			throw new InputValidationException("length", type, minLength, maxLength);
		}
	}
}
