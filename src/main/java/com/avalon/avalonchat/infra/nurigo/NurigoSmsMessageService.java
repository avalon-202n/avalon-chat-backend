package com.avalon.avalonchat.infra.nurigo;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.avalon.avalonchat.configuration.nurigo.NurigoProperties;
import com.avalon.avalonchat.domain.user.service.SmsMessageService;

public class NurigoSmsMessageService implements SmsMessageService {

	private final DefaultMessageService nurigoMessageService;
	private final String from;

	public NurigoSmsMessageService(NurigoProperties properties) {
		this.nurigoMessageService = NurigoApp.INSTANCE.initialize(
			properties.getApiKey(),
			properties.getApiSecret(),
			properties.getDomain()
		);
		this.from = properties.getFromNumber();
	}

	@Override
	public void sendAuthenticationCode(String to, String code) {
		Message message = new Message();
		message.setFrom(from);
		message.setTo(to);
		message.setText("인증코드 " + code + " 를 입력하세요.");

		try {
			nurigoMessageService.send(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
