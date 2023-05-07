package com.avalon.avalonchat.infra.redis;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.user.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.domain.user.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@Transactional
@SpringBootTest
class RedisPhoneNumberKeyAuthCodeValueStoreTest extends BaseTestContainerTest {

	@Autowired
	private RedisPhoneNumberKeyAuthCodeValueStore sut;

	@Test
	void get_set() {
		// given
		PhoneNumberKey key = PhoneNumberKey.fromString("01012345678");
		String value = "cert-code";

		// when
		sut.put(key, value);
		AuthCodeValue authCodeValue = sut.get(key);
		AuthCodeValue nullCodeValue = sut.get(PhoneNumberKey.fromString("01099999999"));

		// then
		assertThat(authCodeValue.isAuthenticated()).isFalse();
		assertThat(authCodeValue.matches(value)).isTrue();
		assertThat(nullCodeValue).isNull();
	}
}
