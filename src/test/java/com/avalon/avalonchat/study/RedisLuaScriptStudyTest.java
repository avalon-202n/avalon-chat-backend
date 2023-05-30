package com.avalon.avalonchat.study;

import static java.util.List.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Disabled
public class RedisLuaScriptStudyTest {

	RedisTemplate<String, String> redisTemplate;
	ValueOperations<String, String> valueOp;

	@BeforeEach
	void setUp() {
		// setup
		redisTemplate = new RedisTemplate<>();
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
		connectionFactory.afterPropertiesSet();

		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();

		valueOp = redisTemplate.opsForValue();
	}

	@Test
	void basic_script_1() {
		RedisScript<Boolean> script = RedisScript.of(
			"return true",
			Boolean.class
		);

		Boolean result = redisTemplate.execute(script, Collections.emptyList());

		assertThat(result).isTrue();
	}

	@Test
	void basic_script_2() {
		RedisScript<Boolean> script = RedisScript.of(
			"return tonumber(ARGV[1]) > 50",
			Boolean.class
		);

		assertThat(redisTemplate.execute(script, Collections.emptyList(), "50")).isFalse();
		assertThat(redisTemplate.execute(script, Collections.emptyList(), "51")).isTrue();
	}

	// 주어진 key 에 대한 인증 여부를 응답하는 script
	@Test
	void script_1() {
		String luaScript = "local value = redis.call('GET', KEYS[1]) "
			+ " return value ~= nil and value == 'AUTHCODE::[AUTHENTICATED]' ";

		RedisScript<Boolean> redisScript = RedisScript.of(
			luaScript,
			Boolean.class
		);
		valueOp.set("PHONENUMBER::01012345678", "AUTHCODE::123456");
		valueOp.set("PHONENUMBER::01011112222", "AUTHCODE::[AUTHENTICATED]");

		assertThat(redisTemplate.execute(redisScript, of("PHONENUMBER::01012345678"))).isFalse();
		assertThat(redisTemplate.execute(redisScript, of("PHONENUMBER::01011112222"))).isTrue();
		assertThat(redisTemplate.execute(redisScript, of("PHONENUMBER::11112222333"))).isFalse();
	}

	// 주어진 key, value 에 대해 매칭 여부를 판단.
	// 매치하는 경우 AUTHENTICATED 로 업데이트.
	// 매치 여부를 응답하는 script
	@Test
	void script_2() {
		String luaScript = "local value = redis.call('GET', KEYS[1]) "
			+ " if value == ARGV[1] "
			+ " then "
			+ "  redis.call('SET', KEYS[1], 'AUTHCODE::[AUTHENTICATED]') "
			+ "  return true "
			+ " end "
			+ " return false";

		RedisScript<Boolean> redisScript = RedisScript.of(
			luaScript,
			Boolean.class
		);
		valueOp.set("PHONENUMBER::01012345678", "AUTHCODE::123456");
		valueOp.set("PHONENUMBER::01011112222", "AUTHCODE::[AUTHENTICATED]");

		assertThat(redisTemplate.execute(redisScript, of("PHONENUMBER::01012345678"), "AUTHCODE::123456")).isTrue();
		assertThat(redisTemplate.execute(redisScript, of("PHONENUMBER::01011112222"), "AUTHCODE::123456")).isFalse();
		assertThat(redisTemplate.execute(redisScript, of("PHONENUMBER::11112222333"), "AUTHCODE::123456")).isFalse();

		String value1 = valueOp.get("PHONENUMBER::01012345678");
		String value2 = valueOp.get("PHONENUMBER::01011112222");
		String value3 = valueOp.get("PHONENUMBER::11112222333");

		assertThat(value1).isEqualTo("AUTHCODE::[AUTHENTICATED]");
		assertThat(value2).isEqualTo("AUTHCODE::[AUTHENTICATED]");
		assertThat(value3).isNull();
	}
}
