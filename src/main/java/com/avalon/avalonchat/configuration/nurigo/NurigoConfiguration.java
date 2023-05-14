package com.avalon.avalonchat.configuration.nurigo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avalon.avalonchat.infra.nurigo.NurigoSmsMessageService;

@Configuration(proxyBeanMethods = false)
public class NurigoConfiguration {

	@Bean
	public NurigoSmsMessageService nurigoSmsMessageService(NurigoProperties properties) {
		return new NurigoSmsMessageService(properties);
	}
}
