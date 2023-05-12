package com.avalon.avalonchat.core.user.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.testsupport.Fixture;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void User_저장_성공() {
		//given
		User user = Fixture.createUser();

		//when
		userRepository.save(user);

		//then
		User foundUser = userRepository.findAll().get(0);
		assertThat(foundUser.getId()).isNotNull();
		assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
		assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
		assertThat(foundUser.getCreatedAt()).isNotNull();
		assertThat(foundUser.getUpdatedAt()).isNotNull();
	}
}
