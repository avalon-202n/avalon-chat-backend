package com.avalon.avalonchat.domain.user.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

	@DisplayName("user 생성 성공")
	@Test
	void test1() {
		//given when
		User user = new User(
			"avalon@e.com",
			"passW0rd"
		);

		//then
		assertThat(user).isNotNull();
	}
}
