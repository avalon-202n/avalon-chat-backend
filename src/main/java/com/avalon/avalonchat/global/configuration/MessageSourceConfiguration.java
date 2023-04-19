package com.avalon.avalonchat.global.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import com.avalon.avalonchat.global.util.MessageUtils;

@Configuration(proxyBeanMethods = false)
public class MessageSourceConfiguration {

	@Bean
	public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
		MessageUtils.setMessageSourceAccessor(messageSourceAccessor);
		return messageSourceAccessor;
	}
}
