package com.avalon.avalonchat.domain.user.service;

public interface MessageService {
	void sendAuthenticationCode(String toNumber, String code);
}
