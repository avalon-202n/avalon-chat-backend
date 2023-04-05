package com.avalon.avalonchat.domain.friend.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.Fixture;

@DataJpaTest
class FriendServiceImplTest {
	private FriendServiceImpl friendService;
	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		friendService = new FriendServiceImpl(friendRepository, profileRepository);
	}

	@Test
	@Transactional
	void 친구추가_성공() {
		// given
		String phoneNumber1 = "010-1234-5678";
		String phoneNumber2 = "010-8765-4321";
		String[] phoneNumbers = {phoneNumber1, phoneNumber2};
		FriendAddRequest request = DtoFixture.friendAddRequest(phoneNumbers);

		User myUser = Fixture.createUser("myUser1@gmail.com", "myUser1");
		User friendUser1 = Fixture.createUser("friendUser1@gmail.com", "friendUser1");
		User friendUser2 = Fixture.createUser("friendUser2@gmail.com", "friendUser2");
		User savedMyUser = userRepository.save(myUser);
		User savedFriendUser1 = userRepository.save(friendUser1);
		User savedFriendUser2 = userRepository.save(friendUser2);

		Profile myProfile = new Profile(savedMyUser, "bio", LocalDate.now(), "nickname");
		Profile friendProfile1 = new Profile(savedFriendUser1, "bio1", LocalDate.now(), "nickname1");
		Profile friendProfile2 = new Profile(savedFriendUser2, "bio2", LocalDate.now(), "nickname2");
		friendProfile1.setPhoneNumber(phoneNumber1);
		friendProfile2.setPhoneNumber(phoneNumber2);
		Profile savedMyProfile = profileRepository.save(myProfile);
		profileRepository.save(friendProfile1);
		profileRepository.save(friendProfile2);

		// when
		FriendAddResponse response = friendService.addFriend(savedMyProfile.getId(), request);

		// then
		assertThat(response.getFriendIds().size()).isEqualTo(2);
	}
}
