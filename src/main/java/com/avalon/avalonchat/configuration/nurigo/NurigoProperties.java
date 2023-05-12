package com.avalon.avalonchat.configuration.nurigo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "cool-sms")
public class NurigoProperties {
	private final String apiKey;
	private final String apiSecret;
	private final String fromNumber;
	private final String domain;

	public NurigoProperties(String apiKey, String apiSecret, String fromNumber, String domain) {
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.fromNumber = fromNumber.replaceAll("-", "").trim();
		this.domain = domain;
	}
}
