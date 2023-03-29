package com.avalon.avalonchat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
class AvalonChatBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
