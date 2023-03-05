package com.avalon.avalonchat.global.error;

import java.util.regex.Pattern;
import java.util.Collection;

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

}
