package com.avalon.avalonchat.core.friend.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;

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
		User myUser = new User(Email.of("email1@gmail.com"), Password.of("password1"));
		User friendUser = new User(Email.of("email2@gmail.com"), Password.of("password2"));

		String phoneNumber = "01055110625";

		Profile myProfile = new Profile(myUser, "bio1", LocalDate.now(), "nickname1", phoneNumber);
		Profile friendProfile = new Profile(friendUser, "bio2", LocalDate.now(), "nickname2", phoneNumber);

		Friend friend = new Friend(myProfile, friendProfile, "nickname2");

		// when
		Friend savedFriend = friendRepository.save(friend);

		// then
		assertThat(savedFriend.getMyProfile()).isEqualTo(myProfile);
		assertThat(savedFriend.getFriendProfile()).isEqualTo(friendProfile);
	}

	@Test
	void Friend_Profile_다대일_매핑_성공() {
		// given
		User myUser = new User(Email.of("email1@gmail.com"), Password.of("password1"));
		Profile myProfile = new Profile(myUser, "bio1", LocalDate.now(), "nickname1", "01055110626");

		User friendUser1 = new User(Email.of("email2@gmail.com"), Password.of("password2"));
		Profile friendProfile1 = new Profile(friendUser1, "bio2", LocalDate.now(), "nickname2", "01055110627");
		User friendUser2 = new User(Email.of("email3@gmail.com"), Password.of("password3"));
		Profile friendProfile2 = new Profile(friendUser2, "bio4", LocalDate.now(), "nickname4", "01055110628");

		Friend friend1 = new Friend(myProfile, friendProfile1, "nickname2");
		Friend friend2 = new Friend(myProfile, friendProfile2, "nickname4");

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
		User myUser = new User(Email.of("email@gmail.com"), Password.of("password"));
		Profile myProfile = new Profile(myUser, "bio", LocalDate.now(), "nickname", "01055110625");

		userRepository.save(myUser);
		profileRepository.save(myProfile);

		List<Friend> friends = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			User friendUser = new User(Email.of("email@gmail" + i + ".com"), Password.of("password" + i));
			Profile friendProfile = new Profile(friendUser, "bio", LocalDate.now(), "nickname", "01055110626" + i);
			Friend friend = new Friend(myProfile, friendProfile, "nickname");

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
	void myProfileId와_friendProfileId로_친구찾기성공() {
		// given - ready for users & profiles
		User myUser = new User(Email.of("myUser@gmail.com"), Password.of("myPassword"));
		Profile myProfile = new Profile(myUser, "myBio", LocalDate.now(), "myProfile", "01012345678");

		User friendUser = new User(Email.of("friendUser@gmail.com"), Password.of("friendPassword"));
		Profile friendProfile = new Profile(friendUser, "friendBio", LocalDate.now(), "friendNickname", "01012123434");

		userRepository.save(myUser);
		userRepository.save(friendUser);
		Profile savedMyProfile = profileRepository.save(myProfile);
		Profile savedFriendProfile = profileRepository.save(friendProfile);

		// given - ready for friend
		Friend friend = new Friend(savedMyProfile, savedFriendProfile, "friendNickname");
		friendRepository.save(friend);

		// when
		Friend foundFriend = friendRepository.findByMyProfileIdAndFriendProfileId(savedMyProfile.getId(),
			savedFriendProfile.getId()).get();

		// then
		assertThat(foundFriend.getMyProfile().getId()).isEqualTo(savedMyProfile.getId());
		assertThat(foundFriend.getFriendProfile().getId()).isEqualTo(savedFriendProfile.getId());
	}
}
