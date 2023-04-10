package com.avalon.avalonchat.testsupport.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

public class RedisExtension implements BeforeAllCallback {

	@Override
	public void beforeAll(ExtensionContext context) {
		DockerImageName redisImageName = DockerImageName.parse("redis:6-alpine");
		RedisContainer redisContainer = new RedisContainer(redisImageName)
			.withExposedPorts(6379)
			.withReuse(true);
		redisContainer.start();

		System.setProperty("spring.redis.host", redisContainer.getHost());
		System.setProperty("spring.redis.port", redisContainer.getMappedPort(6379).toString());
	}
}
