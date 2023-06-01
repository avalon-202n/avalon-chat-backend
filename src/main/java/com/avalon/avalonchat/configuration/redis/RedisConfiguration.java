package com.avalon.avalonchat.configuration.redis;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static java.nio.charset.StandardCharsets.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.infra.redis.RedisPhoneNumberAuthCodeStore;

import redis.embedded.RedisServer;

@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {



	@Bean
	public RedisConnectionFactory redisConnectionFactory(RedisProperties properties) {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(
			properties.getHost(),
			properties.getPort()
		);
		return new LettuceConnectionFactory(configuration);
	}

	@Bean
	public PhoneNumberAuthCodeStore phoneNumberAuthCodeStore(RedisConnectionFactory connectionFactory) {
		RedisTemplate<PhoneNumberKey, AuthCodeValue> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new PhoneNumberKeySerializer());
		redisTemplate.setValueSerializer(new AuthCodeValueSerializer());
		redisTemplate.setEnableTransactionSupport(false);
		redisTemplate.afterPropertiesSet();
		return new RedisPhoneNumberAuthCodeStore(redisTemplate);
	}

	private static class PhoneNumberKeySerializer implements RedisSerializer<PhoneNumberKey> {

		@Override
		public byte[] serialize(PhoneNumberKey phoneNumberKey) throws SerializationException {
			checkNotNullForSerializer(phoneNumberKey, "phoneNumberKey");
			return phoneNumberKey.toString().getBytes(UTF_8);
		}

		@Override
		public PhoneNumberKey deserialize(byte[] bytes) throws SerializationException {
			return bytes == null ? null : PhoneNumberKey.fromString(new String(bytes, UTF_8));
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
			return bytes == null ? null : AuthCodeValue.fromString(new String(bytes, UTF_8));
		}
	}

	@Configuration(proxyBeanMethods = false)
	static class EmbeddedRedisConfiguration {

		private final RedisServer redisServer;

		public EmbeddedRedisConfiguration(RedisProperties redisProperties) {
			this.redisServer = new RedisServer(6381);
		}

		@PostConstruct
		public void postConstruct() {
			redisServer.start();
		}

		@PreDestroy
		public void preDestroy() {
			redisServer.stop();
		}
	}
}
