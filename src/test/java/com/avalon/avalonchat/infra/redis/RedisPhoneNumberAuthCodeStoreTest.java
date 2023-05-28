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
	void isAuthenticated() {
		// given
		PhoneNumberKey key1 = PhoneNumberKey.fromString("01011111111");
		PhoneNumberKey key2 = PhoneNumberKey.fromString("01022222222");
		PhoneNumberKey key3 = PhoneNumberKey.fromString("01033333333");

		// when
		sut.save(key1, AuthCodeValue.ofUnauthenticated("cert-code"));
		sut.save(key2, AuthCodeValue.ofAuthenticated());

		boolean authenticated1 = sut.isAuthenticated(key1);
		boolean authenticated2 = sut.isAuthenticated(key2);
		boolean authenticated3 = sut.isAuthenticated(key3);

		// then
		assertThat(authenticated1).isFalse();
		assertThat(authenticated2).isTrue();
		assertThat(authenticated3).isFalse();
	}

	@Test
	void checkKeyValueMatches() {
		// given
		PhoneNumberKey key1 = PhoneNumberKey.fromString("01011111111");
		PhoneNumberKey key2 = PhoneNumberKey.fromString("01022222222");
		PhoneNumberKey key3 = PhoneNumberKey.fromString("01033333333");

		// when
		sut.save(key1, AuthCodeValue.ofUnauthenticated("123456"));
		sut.save(key2, AuthCodeValue.ofAuthenticated());

		boolean match1 = sut.checkKeyValueMatches(key1, AuthCodeValue.ofUnauthenticated("123456"));
		boolean match2 = sut.checkKeyValueMatches(key2, AuthCodeValue.ofUnauthenticated("123456"));
		boolean match3 = sut.checkKeyValueMatches(key3, AuthCodeValue.ofUnauthenticated("123456"));

		// then
		assertThat(match1).isTrue();
		assertThat(match2).isFalse();
		assertThat(match3).isFalse();

		assertThat(sut.isAuthenticated(key1)).isTrue(); // key1 updated to authenticated
		assertThat(sut.isAuthenticated(key2)).isTrue();
		assertThat(sut.isAuthenticated(key3)).isFalse();
	}
}
