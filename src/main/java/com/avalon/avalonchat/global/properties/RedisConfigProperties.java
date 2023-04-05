package com.avalon.avalonchat.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cache.redis")
@Configuration
public class RedisConfigProperties {
	private String host;
	private int port;
}
