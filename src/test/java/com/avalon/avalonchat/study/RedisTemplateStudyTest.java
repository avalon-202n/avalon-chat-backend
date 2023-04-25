package com.avalon.avalonchat.study;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Disabled("학습용 테스트 - 학습 할때만 enable")
public class RedisTemplateStudyTest {

	static class CodeValue {

		private static final String PREFIX = "CODE::";

		String value;

		public CodeValue(String value) {
			this.value = value;
		}

		static CodeValue fromString(String key) {
			String value = key.replace(PREFIX, "");
			return new CodeValue(value);
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return PREFIX + value;
		}
	}

	static class CodeSerializer implements RedisSerializer<CodeValue> {

		private final Charset UTF_8 = StandardCharsets.UTF_8;

		@Override
		public byte[] serialize(CodeValue code) throws SerializationException {
			if (code == null) {
				throw new SerializationException("code is null");
			}
			return code.toString().getBytes(UTF_8);
		}

		@Override
		public CodeValue deserialize(byte[] bytes) throws SerializationException {
			if (bytes == null) {
				throw new SerializationException("bytes is null");
			}
			return CodeValue.fromString(new String(bytes, UTF_8));
		}
	}

	@Nested
	class ValueOpStudy {

		@Nested
		class StringStringValueOperation {
			ValueOperations<String, String> valueOp;

			@BeforeEach
			void setUp() {
				// setup
				RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
				RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
				configuration.setPassword(RedisPassword.of("avalon"));
				LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
				connectionFactory.afterPropertiesSet();

				redisTemplate.setConnectionFactory(connectionFactory);
				redisTemplate.setKeySerializer(new StringRedisSerializer());
				redisTemplate.setValueSerializer(new StringRedisSerializer());
				redisTemplate.afterPropertiesSet();

				// given
				valueOp = redisTemplate.opsForValue();
			}

			@Test
			void set_and_get() {
				// when
				valueOp.set("hello", "world");
				valueOp.set("world", "hello");

				// then
				assertThat(valueOp.get("hello")).isEqualTo("world");
				assertThat(valueOp.get("world")).isEqualTo("hello");
			}

			@Test
			void set_and_get2() {
				// when
				valueOp.set("hello", "world");
				valueOp.set("hello", "world2");

				// then
				assertThat(valueOp.get("hello")).isEqualTo("world2");
				assertThat(valueOp.get("hello", 0, 1)).isEqualTo("wo");
			}
		}

		@Nested
		class StringCodeValueOperation {
			ValueOperations<String, CodeValue> valueOp;

			@BeforeEach
			void setUp() {
				// setup
				RedisTemplate<String, CodeValue> redisTemplate = new RedisTemplate<>();
				RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
				configuration.setPassword(RedisPassword.of("avalon"));
				LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
				connectionFactory.afterPropertiesSet();

				redisTemplate.setConnectionFactory(connectionFactory);
				redisTemplate.setKeySerializer(new StringRedisSerializer());
				redisTemplate.setValueSerializer(new CodeSerializer());
				redisTemplate.afterPropertiesSet();

				// given
				valueOp = redisTemplate.opsForValue();
			}

			@Test
			void set_and_get_for_custom_class() {
				// given
				CodeValue code = new CodeValue("ABCDEF");

				// when
				valueOp.set("key", code, 10, TimeUnit.MINUTES);
				CodeValue foundCode = valueOp.get("key");

				//then
				assertThat(foundCode).isNotNull();
				assertThat(foundCode.getValue()).isEqualTo("ABCDEF");
			}
		}
	}
}
