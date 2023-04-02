package com.avalon.avalonchat.global.util;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ResponseEntityUtil {

	public static <T> ResponseEntity<T> created(T responseBody) {
		return new ResponseEntity<>(responseBody, CREATED);
	}

	public static ResponseEntity<Void> noContent() {
		return new ResponseEntity<>(NO_CONTENT);
	}
}
