package com.avalon.avalonchat.core.user.application;

public interface SmsMessageService {
	void sendAuthenticationCode(String toNumber, String code);
}
