package com.avalon.avalonchat.domain.auth.service;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoolSmsMessageServiceTest {
	@Autowired
	private MessageService messageService;

	@Test
	void 인증문자전송_성공() {
		// given
		StringBuffer code = new StringBuffer();
		Random random = new Random();

		for (int i = 0; i < 6; i++) {
			code.append((random.nextInt(10)));
		}

		// when & then
		messageService.sendMessage("01055110625", code.toString());
	}
}
