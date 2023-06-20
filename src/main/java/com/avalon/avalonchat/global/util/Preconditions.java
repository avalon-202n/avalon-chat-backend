package com.avalon.avalonchat.global.util;

import com.avalon.avalonchat.global.error.exception.InputValidationException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor
public final class Preconditions {

	public static void checkNotNull(Object value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
	}

	public static void checkNotNullForSerializer(Object target, String name) {
		if (target == null) {
			throw new SerializationException(name + " cannot be null");
		}
	}

	public static void checkPatternMatches(Pattern pattern, String value) {
		log.info("확인! checkPatternMatches -> patter : {}, value : {}", pattern, value);
		if (value == null) {
			throw new NullPointerException("hey, dumb! value is null");
		}
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
