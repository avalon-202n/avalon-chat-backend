package com.avalon.avalonchat.domain.auth.dto;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumber;

import lombok.Getter;

public class AuthPhoneNumberResponse {
	@Getter
	public static class Get {
		private final String phoneNumber;

		public Get(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public static Get ofEntity(AuthPhoneNumber authPhoneNumber) {
			return new Get(authPhoneNumber.getPhoneNumber());
		}
	}

	@Getter
	public static class Compare {
		private final boolean isValidated;

		public Compare(boolean isValidated) {
			this.isValidated = isValidated;
		}

		public static Compare ofEntity(boolean isValidated) {
			return new Compare(isValidated);
		}
	}
}
