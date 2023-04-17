package com.avalon.avalonchat.global.util;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;

import java.util.regex.Pattern;

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
			throw new IllegalArgumentException("Pattern " + pattern + " does not matches " + value);
		}
	}

	public static void checkLength(int minLength, int maxLength, String value, String messageType) {
		if (value.length() < minLength || value.length() > maxLength) {
			if ("password.length".equals(messageType)) {
				throw new IllegalArgumentException(INVALID_LENGTH.getMessage("Password", minLength, maxLength));
			}
		}
	}

}
