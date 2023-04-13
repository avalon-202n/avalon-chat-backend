package com.avalon.avalonchat.domain.user.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "phoneNumber")
@RedisHash(value = "phone_number_authentication_code", timeToLive = 1800)
public class PhoneNumberAuthenticationCode {
	@Id
	private String phoneNumber;
	private String certificationCode;
	private boolean authenticated;

	public PhoneNumberAuthenticationCode(String phoneNumber, String certificationCode) {
		checkNotNull(phoneNumber, "PhoneNumberAuthenticationCode.phoneNumber cannot be null");
		checkNotNull(certificationCode, "PhoneNumberAuthenticationCode.certificationCode cannot be null");

		this.phoneNumber = phoneNumber;
		this.certificationCode = certificationCode;
		this.authenticated = false;
	}

	@PersistenceCreator
	public PhoneNumberAuthenticationCode() {
	}

	public void authenticate() {
		this.authenticated = true;
	}
}
