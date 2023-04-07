package com.avalon.avalonchat.domain.friend.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

class FriendTest {

	@Test
	void Friend_생성_성공() {
		//given
		User myUser = new User(Email.of("email1@gmail.com"), "password1");
		User friendUser = new User(Email.of("email2@gmail.com"), "password2");

		Profile myProfile = new Profile(myUser, "bio1", LocalDate.now(), "nickname1");
		Profile friendProfile = new Profile(friendUser, "bio2", LocalDate.now(), "nickname2");

		//when
		Friend friend = new Friend(myProfile, friendProfile);

		//then
		assertThat(friend.getMyProfile()).isEqualTo(myProfile);
		assertThat(friend.getFriendProfile()).isEqualTo(friendProfile);
		assertThat(friend.getFriendStatus()).isEqualTo(Friend.FriendStatus.NORMAL);
	}

}
