package com.avalon.avalonchat.domain.user.service;

public interface SmsMessageService {
	void sendAuthenticationCode(String toNumber, String code);
}
