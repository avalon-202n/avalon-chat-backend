package com.avalon.avalonchat.global.configuration;

import static java.nio.charset.StandardCharsets.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.avalon.avalonchat.domain.user.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.domain.user.keyvalue.EmailKey;
import com.avalon.avalonchat.domain.user.keyvalue.KeyAuthCodeValueStore;
import com.avalon.avalonchat.domain.user.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.infra.redis.RedisEmailKeyAuthCodeValueStore;
import com.avalon.avalonchat.infra.redis.RedisPhoneNumberKeyAuthCodeValueStore;

@Configuration(proxyBeanMethods = false)
public class KeyValueStoreConfiguration {

	private static void checkNotNullForSerializer(Object target, String name) {
		if (target == null) {
			throw new SerializationException(name + " cannot be null");
		}
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
		configuration.setPassword(RedisPassword.of("avalon"));
		return new LettuceConnectionFactory(configuration);
	}

	@Bean
	public KeyAuthCodeValueStore<EmailKey> emailAuthCodeStore(RedisConnectionFactory connectionFactory) {
		RedisTemplate<EmailKey, AuthCodeValue> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new EmailKeySerializer());
		redisTemplate.setValueSerializer(new AuthCodeValueSerializer());
		return new RedisEmailKeyAuthCodeValueStore(redisTemplate);
	}

	@Bean
	public KeyAuthCodeValueStore<PhoneNumberKey> phoneNumberAuthCodeStore(RedisConnectionFactory connectionFactory) {
		RedisTemplate<PhoneNumberKey, AuthCodeValue> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new PhoneNumberKeySerializer());
		redisTemplate.setValueSerializer(new AuthCodeValueSerializer());
		return new RedisPhoneNumberKeyAuthCodeValueStore(redisTemplate);
	}

	private static class EmailKeySerializer implements RedisSerializer<EmailKey> {

		@Override
		public byte[] serialize(EmailKey emailKey) throws SerializationException {
			checkNotNullForSerializer(emailKey, "emailKey");
			return emailKey.toString().getBytes(UTF_8);
		}

		@Override
		public EmailKey deserialize(byte[] bytes) throws SerializationException {
			checkNotNullForSerializer(bytes, "bytes");
			return EmailKey.fromString(new String(bytes, UTF_8));
		}
	}

	private static class PhoneNumberKeySerializer implements RedisSerializer<PhoneNumberKey> {

		@Override
		public byte[] serialize(PhoneNumberKey phoneNumberKey) throws SerializationException {
			checkNotNullForSerializer(phoneNumberKey, "phoneNumberKey");
			return phoneNumberKey.toString().getBytes(UTF_8);
		}

		@Override
		public PhoneNumberKey deserialize(byte[] bytes) throws SerializationException {
			checkNotNullForSerializer(bytes, "bytes");
			return PhoneNumberKey.fromString(new String(bytes, UTF_8));
		}
	}

	private static class AuthCodeValueSerializer implements RedisSerializer<AuthCodeValue> {

		@Override
		public byte[] serialize(AuthCodeValue authCodeValue) throws SerializationException {
			checkNotNullForSerializer(authCodeValue, "authCodeValue");
			return authCodeValue.toString().getBytes(UTF_8);
		}

		@Override
		public AuthCodeValue deserialize(byte[] bytes) throws SerializationException {
			checkNotNullForSerializer(bytes, "bytes");
			return AuthCodeValue.fromString(new String(bytes, UTF_8));
		}
	}
}
