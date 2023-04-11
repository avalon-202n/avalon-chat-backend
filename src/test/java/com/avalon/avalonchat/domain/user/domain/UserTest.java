package com.avalon.avalonchat.domain.user.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void User_생성_성공() {
		//given when
		User user = new User(
			Email.of("avalon@e.com"),
			Password.of("passW0rd")
		);

		//then
		assertThat(user).isNotNull();
	}
}
