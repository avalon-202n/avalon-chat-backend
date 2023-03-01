package com.avalon.avalonchat.domain.user.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void user_생성_성공() {
		//given when
		User user = new User(
			"avalon@e.com",
			"passW0rd",
			"happy"
		);

		//then
		assertThat(user).isNotNull();
	}
}
