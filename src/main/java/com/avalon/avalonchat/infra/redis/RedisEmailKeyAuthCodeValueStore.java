package com.avalon.avalonchat.infra.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.avalon.avalonchat.core.user.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.keyvalue.EmailKey;
import com.avalon.avalonchat.core.user.keyvalue.KeyAuthCodeValueStore;

public class RedisEmailKeyAuthCodeValueStore implements KeyAuthCodeValueStore<EmailKey> {

	private final ValueOperations<EmailKey, AuthCodeValue> emailAuthOperations;

	public RedisEmailKeyAuthCodeValueStore(RedisTemplate<EmailKey, AuthCodeValue> emailAuthRedisTemplate) {
		this.emailAuthOperations = emailAuthRedisTemplate.opsForValue();
	}

	@Override
	public void put(EmailKey key, AuthCodeValue value) {
		emailAuthOperations.set(key, value, Duration.ofMinutes(30));
	}

	@Override
	public AuthCodeValue get(EmailKey key) {
		return emailAuthOperations.get(key);
	}
}
