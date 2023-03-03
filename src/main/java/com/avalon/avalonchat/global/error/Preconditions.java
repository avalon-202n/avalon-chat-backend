package com.avalon.avalonchat.global.error;

import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class Preconditions {

	public static void checkPatternMatches(Pattern pattern, String value) {
		if (!pattern.matcher(value).find()) {
			throw new IllegalArgumentException("Pattern " + pattern + " does not matches " + value);
		}
	}
}
