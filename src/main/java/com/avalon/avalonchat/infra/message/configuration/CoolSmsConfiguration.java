package com.avalon.avalonchat.infra.message.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CoolSmsConfiguration {
	private final CoolSmsProperties properties;

	@Bean
	public DefaultMessageService defaultMessageService() {
		return NurigoApp.INSTANCE.initialize(properties.getApiKey(), properties.getApiSecret(), properties.getDomain());
	}
}
