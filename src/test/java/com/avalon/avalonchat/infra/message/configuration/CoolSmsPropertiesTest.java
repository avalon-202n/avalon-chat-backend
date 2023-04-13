package com.avalon.avalonchat.infra.message.configuration;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoolSmsPropertiesTest {
	@Autowired
	private CoolSmsProperties properties;

	@Test
	void CoolSms_속성주입_성공() {
		assertThat(properties.getApiKey()).isNotNull();
		assertThat(properties.getApiSecret()).isNotNull();
		assertThat(properties.getFromNumber()).isNotNull();
		assertThat(properties.getDomain()).isNotNull();
	}
}
