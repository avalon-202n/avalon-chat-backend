package com.avalon.avalonchat.infra.message.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;

@ConfigurationProperties(prefix = "cool-sms")
@ConstructorBinding
@Getter
public class CoolSmsProperties {
	private final String apiKey;
	private final String apiSecret;
	private final String fromNumber;
	private final String domain;

	public CoolSmsProperties(String apiKey, String apiSecret, String fromNumber, String domain) {
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.fromNumber = fromNumber.replaceAll("-", "").trim();
		this.domain = domain;
	}
}
