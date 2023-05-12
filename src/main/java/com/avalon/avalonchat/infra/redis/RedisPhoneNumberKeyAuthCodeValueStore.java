package com.avalon.avalonchat.infra.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.avalon.avalonchat.core.user.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.keyvalue.KeyAuthCodeValueStore;
import com.avalon.avalonchat.core.user.keyvalue.PhoneNumberKey;

public class RedisPhoneNumberKeyAuthCodeValueStore implements KeyAuthCodeValueStore<PhoneNumberKey> {

	private final ValueOperations<PhoneNumberKey, AuthCodeValue> phoneNumberAuthOperations;

	public RedisPhoneNumberKeyAuthCodeValueStore(
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
