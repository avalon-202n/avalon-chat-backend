package com.avalon.avalonchat.domain.user.keyvalue;

import static lombok.AccessLevel.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public class AuthCodeValue {

	public static final String AUTHENTICATED = "[AUTHENTICATED]";
	private static final String PREFIX = "AUTH_CODE::";
	private final String certificationCode;

	private final boolean authenticated;

	public static AuthCodeValue ofAuthenticated() {
		return new AuthCodeValue(AUTHENTICATED, true);
	}

	public static AuthCodeValue ofUnauthenticated(String certificationCode) {
		return new AuthCodeValue(certificationCode, false);
	}

	public static AuthCodeValue fromString(String string) {
		String value = string.replace(PREFIX, "");
		if (value.equals(AUTHENTICATED)) {
			return AuthCodeValue.ofAuthenticated();
		} else {
			return AuthCodeValue.ofUnauthenticated(value);
		}
	}

	public boolean matches(String certificationCode) {
		return this.certificationCode.equals(certificationCode);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(PREFIX);
		if (authenticated) {
			sb.append(AUTHENTICATED);
		} else {
			sb.append(certificationCode);
		}
		return sb.toString();
	}
}
