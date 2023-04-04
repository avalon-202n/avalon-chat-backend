package com.avalon.avalonchat.domain.friend.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@DataJpaTest
@Transactional
class FriendRepositoryTest {

	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void Friend_테이블_중복매핑_엔티티_저장_성공() {
		// given
		User myUser = new User(Email.of("email1@gmail.com"), "password1");
		User friendUser = new User(Email.of("email2@gmail.com"), "password2");

		Profile myProfile = new Profile(myUser, "bio1", LocalDate.now(), "nickname1");
		Profile friendProfile = new Profile(friendUser, "bio2", LocalDate.now(), "nickname2");

		Friend friend = new Friend(myProfile, friendProfile, Friend.FriendStatus.NORMAL);

		// when
		Friend savedFriend = friendRepository.save(friend);

		// then
		assertThat(savedFriend.getMyProfile()).isEqualTo(myProfile);
		assertThat(savedFriend.getFriendProfile()).isEqualTo(friendProfile);
	}

	@Test
	void Friend_Profile_다대일_매핑_성공() {
		// given
		User myUser = new User(Email.of("email1@gmail.com"), "password1");
		Profile myProfile = new Profile(myUser, "bio1", LocalDate.now(), "nickname1");

		User friendUser1 = new User(Email.of("email2@gmail.com"), "password2");
		Profile friendProfile1 = new Profile(friendUser1, "bio2", LocalDate.now(), "nickname2");
		User friendUser2 = new User(Email.of("email3@gmail.com"), "password3");
		Profile friendProfile2 = new Profile(friendUser2, "bio4", LocalDate.now(), "nickname4");

		Friend friend1 = new Friend(myProfile, friendProfile1, Friend.FriendStatus.NORMAL);
		Friend friend2 = new Friend(myProfile, friendProfile2, Friend.FriendStatus.NORMAL);

		userRepository.save(myUser);
		userRepository.save(friendUser1);
		userRepository.save(friendUser2);

		profileRepository.save(myProfile);
		profileRepository.save(friendProfile1);
		profileRepository.save(friendProfile2);

		friendRepository.save(friend1);
		friendRepository.save(friend2);

		// when
		List<Friend> findFriends = friendRepository.findAll();

		// then
		assertThat(findFriends.get(0).getMyProfile()).isEqualTo(findFriends.get(1).getMyProfile());
		assertThat(findFriends.get(0).getFriendProfile()).isNotEqualTo(findFriends.get(1).getFriendProfile());
	}

	@Test
	void Friend_벌크_저장_성공() {
		// given
		User myUser = new User(Email.of("email@gmail.com"), "password");
		Profile myProfile = new Profile(myUser, "bio", LocalDate.now(), "nickname");

		userRepository.save(myUser);
		profileRepository.save(myProfile);

		List<Friend> friends = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			User friendUser = new User(Email.of("email@gmail" + i + ".com"), "password" + i);
			Profile friendProfile = new Profile(friendUser, "bio", LocalDate.now(), "nickname");
			Friend friend = new Friend(myProfile, friendProfile, Friend.FriendStatus.NORMAL);

			userRepository.save(friendUser);
			profileRepository.save(friendProfile);

			friends.add(friend);
		}

		// when
		friendRepository.saveAll(friends);
		List<Friend> findFriends = friendRepository.findAll();

		// then
		assertThat(findFriends.size()).isEqualTo(10);
	}

	@Test
		//TODO
	void Friend_transactionChunk_벌크_저장_성공() {

	}
}