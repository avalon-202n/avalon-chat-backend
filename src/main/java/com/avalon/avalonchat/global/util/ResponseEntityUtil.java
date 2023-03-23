package com.avalon.avalonchat.global.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ResponseEntityUtil {

	public static ResponseEntity<Void> noContent() {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
