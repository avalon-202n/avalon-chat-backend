package com.avalon.avalonchat.global.configuration.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtConfigProperties {
	private int accessValidity;
	private int refreshValidity;
	private String key;
}
