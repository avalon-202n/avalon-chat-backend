package com.avalon.avalonchat.infra.redis;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;

@Transactional
@SpringBootTest
class RedisPhoneNumberAuthCodeStoreTest {

	@Autowired
	private RedisPhoneNumberAuthCodeStore sut;

	@Test
	void get_set() {
		// given
		PhoneNumberKey key = PhoneNumberKey.fromString("01012345678");
		AuthCodeValue value = AuthCodeValue.ofUnauthenticated("cert-code");

		// when
		sut.save(key, value);
		boolean authenticated1 = sut.isAuthenticated(key);
		boolean authenticated2 = sut.isAuthenticated(PhoneNumberKey.fromString("01099999999"));

		// then
		assertThat(authenticated1).isTrue();
		assertThat(authenticated2).isFalse();
	}
}
