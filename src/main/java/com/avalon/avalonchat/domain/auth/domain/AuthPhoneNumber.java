package com.avalon.avalonchat.domain.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
@RedisHash(value = "auth_phone_number", timeToLive = 1800)
public class AuthPhoneNumber {
	@Id
	private String phoneNumber;
	private String code;
	private boolean isValidated;

	public AuthPhoneNumber(String phoneNumber, String code) {
		this.phoneNumber = phoneNumber;
		this.code = code;
		this.isValidated = false;
	}

	@PersistenceCreator
	public AuthPhoneNumber() {
	}

	public void validate() {
		this.isValidated = true;
	}
}
