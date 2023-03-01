package com.avalon.avalonchat.global.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class Preconditions {

	public static void checkNotNull(Object value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
	}
}
