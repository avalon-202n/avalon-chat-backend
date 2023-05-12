package com.avalon.avalonchat.infra.redis;

import java.time.Duration;

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
	public void put(PhoneNumberKey key, AuthCodeValue value) {
		phoneNumberAuthOperations.set(key, value, Duration.ofMinutes(30));
	}

	@Override
	public AuthCodeValue get(PhoneNumberKey key) {
		return phoneNumberAuthOperations.get(key);
	}
}
