package com.avalon.avalonchat.infra.redis;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisPhoneNumberAuthCodeStore implements PhoneNumberAuthCodeStore {

	private final RedisTemplate<PhoneNumberKey, AuthCodeValue> redisTemplate;

	private final RedisScript<Boolean> isAuthenticatedScript = RedisScript.of(
		"local value = redis.call('GET', KEYS[1]) "
			+ " return value ~= nil and value == 'AUTH_CODE::[AUTHENTICATED]' ",
		Boolean.class
	);

	private final RedisScript<Boolean> checkKeyValueMatchesScript = RedisScript.of(
		"local value = redis.call('GET', KEYS[1]) "
			+ " if value == ARGV[1] "
			+ " then "
			+ "  redis.call('SET', KEYS[1], 'AUTH_CODE::[AUTHENTICATED]') "
			+ "  return true "
			+ " end "
			+ " return false",
		Boolean.class
	);

	@Override
	public void save(PhoneNumberKey key, AuthCodeValue value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public boolean isAuthenticated(PhoneNumberKey key) {
		return redisTemplate.execute(isAuthenticatedScript, List.of(key));
	}

	@Override
	public boolean checkKeyValueMatches(PhoneNumberKey key, AuthCodeValue value) {
		return redisTemplate.execute(checkKeyValueMatchesScript, List.of(key), value);
	}
}
