package com.avalon.avalonchat.infra.message;

public interface MessageService {
	void sendAuthenticationCode(String toNumber, String code);
}
