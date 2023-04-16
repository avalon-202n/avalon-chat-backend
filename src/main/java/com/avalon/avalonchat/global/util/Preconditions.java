package com.avalon.avalonchat.global.util;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;

import java.util.Collection;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class Preconditions {

	public static void checkNotNull(Object value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
	}

	public static <T> void checkContains(T value, Collection<T> collection, String message) {
		if (!collection.contains(value)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void checkPatternMatches(Pattern pattern, String value) {
		if (!pattern.matcher(value).find()) {
			throw new IllegalArgumentException("Pattern " + pattern + " does not matches " + value);
		}
	}

	public static void checkLength(int max, int min, String value, String messageType) {
		if (value.length() < min || value.length() > max) {
			if ("password.length".equals(messageType))
				throw new IllegalArgumentException(INVALID_LENGTH.getMessage("Password", max, min));
		}
	}

}
