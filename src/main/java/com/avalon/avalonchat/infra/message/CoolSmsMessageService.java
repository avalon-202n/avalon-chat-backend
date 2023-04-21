package com.avalon.avalonchat.infra.message;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.avalon.avalonchat.domain.user.service.MessageService;
import com.avalon.avalonchat.global.configuration.message.CoolSmsProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoolSmsMessageService implements MessageService {
	private final DefaultMessageService coolSms;
	private final CoolSmsProperties properties;

	@Override
	public void sendAuthenticationCode(String toNumber, String code) {
		Message message = new Message();
		message.setFrom(properties.getFromNumber());
		message.setTo(toNumber);
		message.setText("인증코드 " + code + " 를 입력하세요.");

		try {
			coolSms.send(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
