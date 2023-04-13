package com.avalon.avalonchat.domain.auth.dto;

import lombok.Getter;

public class AuthPhoneNumberRequest {
	@Getter
	public static class Get {
		private final String phoneNumber;

		public Get(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	}

	@Getter
	public static class Compare {
		private final String phoneNumber;
		private final String code;

		public Compare(String phoneNumber, String code) {
			this.phoneNumber = phoneNumber;
			this.code = code;
		}
	}
}
