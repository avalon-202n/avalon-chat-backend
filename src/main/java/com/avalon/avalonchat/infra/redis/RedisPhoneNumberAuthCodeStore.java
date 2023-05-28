package com.avalon.avalonchat.infra.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;

public class RedisPhoneNumberAuthCodeStore implements PhoneNumberAuthCodeStore {

	private final ValueOperations<PhoneNumberKey, AuthCodeValue> phoneNumberAuthOperations;

	public RedisPhoneNumberAuthCodeStore(
		RedisTemplate<PhoneNumberKey, AuthCodeValue> phoneNumberAuthRedisTemplate
	) {
		this.phoneNumberAuthOperations = phoneNumberAuthRedisTemplate.opsForValue();
	}

	@Override
	public void save(PhoneNumberKey key, AuthCodeValue value) {

	}

	@Override
	public boolean isAuthenticated(PhoneNumberKey key) {
		return false;
	}

	@Override
	public boolean checkKeyValueMatches(PhoneNumberKey key, AuthCodeValue value) {
		return false;
	}
}
