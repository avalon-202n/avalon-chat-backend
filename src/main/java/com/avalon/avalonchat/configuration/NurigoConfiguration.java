package com.avalon.avalonchat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.avalon.avalonchat.configuration.nurigo.NurigoProperties;

@Configuration(proxyBeanMethods = false)
public class NurigoConfiguration {

	@Bean
	public DefaultMessageService defaultMessageService(NurigoProperties properties) {
		return NurigoApp.INSTANCE.initialize(
			properties.getApiKey(),
			properties.getApiSecret(),
			properties.getDomain()
		);
	}
}
