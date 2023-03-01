package com.avalon.avalonchat.domain.user.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.testsupport.Fixture;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@DisplayName("user 저장 성공")
	@Test
	void test1() {
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
