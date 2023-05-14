package com.avalon.avalonchat.infra.nurigo;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NurigoSmsMessageServiceTest {
	@Autowired
	private NurigoSmsMessageService messageService;

	@Test
	@Disabled
	void 인증문자_전송_성공() {
		// given
		String code = RandomStringUtils.randomNumeric(6);
		String toNumber = "01055110625";

		// when & then
		messageService.sendAuthenticationCode(toNumber, code);
	}
}
