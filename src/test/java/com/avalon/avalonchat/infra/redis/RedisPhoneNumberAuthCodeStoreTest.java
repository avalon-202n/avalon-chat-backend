package com.avalon.avalonchat.infra.redis;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@Transactional
@SpringBootTest
class RedisPhoneNumberAuthCodeStoreTest extends BaseTestContainerTest {

	@Autowired
	private RedisPhoneNumberAuthCodeStore sut;

	@Test
	void get_set() {
		// given
		PhoneNumberKey key = PhoneNumberKey.fromString("01012345678");
		AuthCodeValue value = AuthCodeValue.ofUnauthenticated("cert-code");

		// when
		sut.put(key, value);
		AuthCodeValue authCodeValue = sut.get(key);
		AuthCodeValue nullCodeValue = sut.get(PhoneNumberKey.fromString("01099999999"));

		// then
		assertThat(authCodeValue.isAuthenticated()).isFalse();
		assertThat(authCodeValue.matches("cert-code")).isTrue();
		assertThat(nullCodeValue).isNull();
	}
}
