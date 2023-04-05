package com.avalon.avalonchat.global.configuration.jwt;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import com.avalon.avalonchat.global.properties.JwtConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtConfigPropertiesTest {

	@Autowired
    JwtConfigProperties jwtConfigProperties;

	@Test
	void jwt_프로퍼티_조회_성공() {
		assertThat(jwtConfigProperties.getAccessValidity()).isNotEqualTo(0);
		assertThat(jwtConfigProperties.getRefreshValidity()).isNotEqualTo(0);
		assertThat(jwtConfigProperties.getKey()).isNotNull();
	}
}
