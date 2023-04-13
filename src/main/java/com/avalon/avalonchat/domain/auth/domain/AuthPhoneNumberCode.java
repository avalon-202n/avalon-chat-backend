package com.avalon.avalonchat.domain.auth.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "phoneNumber")
@RedisHash(value = "auth_phone_number", timeToLive = 1800)
public class AuthPhoneNumberCode {
	@Id
	private String phoneNumber;
	private String code;
	private boolean authenticated;

	public AuthPhoneNumberCode(String phoneNumber, String code) {
		checkNotNull(phoneNumber, "AuthPhoneNumberCode.phoneNumber cannot be null");
		checkNotNull(code, "AuthPhoneNumberCode.code cannot be null");

		this.phoneNumber = phoneNumber;
		this.code = code;
		this.authenticated = false;
	}

	@PersistenceCreator
	public AuthPhoneNumberCode() {
	}

	public void authenticate() {
		this.authenticated = true;
	}
}
