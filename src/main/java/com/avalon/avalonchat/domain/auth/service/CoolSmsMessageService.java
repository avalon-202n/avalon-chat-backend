package com.avalon.avalonchat.domain.auth.service;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.avalon.avalonchat.domain.auth.service.properties.CoolSmsProperties;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoolSmsMessageService implements MessageService {
	private final CoolSmsProperties properties;

	@Override
	public void sendMessage(String toNumber, String code) {
		DefaultMessageService service = NurigoApp.INSTANCE.initialize(properties.getApiKey(), properties.getApiSecret(),
			properties.getDomain());

		Message message = new Message();
		message.setFrom(properties.getFromNumber());
		message.setTo(toNumber);
		message.setText("인증코드 " + code + " 를 입력하세요.");

		try {
			service.send(message);
		} catch (NurigoMessageNotReceivedException e) {
			throw new AvalonChatRuntimeException("인증문자전송 중 에러발생", e);
		} catch (NurigoEmptyResponseException e) {
			throw new AvalonChatRuntimeException("인증문자전송 중 에러발생", e);
		} catch (NurigoUnknownException e) {
			throw new AvalonChatRuntimeException("인증문자전송 중 에러발생", e);
		}
	}
}
