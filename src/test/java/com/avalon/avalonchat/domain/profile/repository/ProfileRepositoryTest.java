package com.avalon.avalonchat.domain.profile.repository;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.testsupport.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProfileRepositoryTest {

	@Autowired
	private ProfileRepository profileRepository;

	@DisplayName("profile 저장 성공")
	@Test
	void save() {
		//given
		Profile profile = Fixture.createProfile();

		//when
		profileRepository.save(profile);

		//then
		Profile foundProfile = profileRepository.findAll().get(0);
		assertThat(foundProfile.getId()).isNotNull();
		assertThat(foundProfile.getBio()).isEqualTo(profile.getBio());
		assertThat(foundProfile.getBirthDate()).isEqualTo(profile.getBirthDate());
		assertThat(foundProfile.getNickname()).isEqualTo(profile.getNickname());
		assertThat(foundProfile.getCreatedAt()).isNotNull();
		assertThat(foundProfile.getUpdatedAt()).isNotNull();
	}
}
