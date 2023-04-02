package com.avalon.avalonchat.global.configuration.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JwtConfigProperties {
	private int accessValidity;
	private int refreshValidity;
	private String key;
}
