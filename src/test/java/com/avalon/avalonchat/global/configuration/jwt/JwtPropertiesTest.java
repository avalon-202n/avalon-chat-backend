package com.avalon.avalonchat.global.configuration.jwt;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.configuration.jwt.JwtProperties;

@SpringBootTest
class JwtPropertiesTest {

	@Autowired
	JwtProperties jwtProperties;

	@Test
	void jwt_프로퍼티_조회_성공() {
		assertThat(jwtProperties.getAccessValidity()).isNotEqualTo(0);
		assertThat(jwtProperties.getRefreshValidity()).isNotEqualTo(0);
		assertThat(jwtProperties.getSecret()).isNotNull();
	}
}
