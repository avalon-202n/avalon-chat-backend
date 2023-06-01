package com.avalon.avalonchat.core.friend.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;

class FriendTest {

	@Test
	void Friend_생성_성공() {
		//given
		User myUser = new User(Email.of("email1@gmail.com"), Password.of("password1"));
		User friendUser = new User(Email.of("email2@gmail.com"), Password.of("password2"));

		String phoneNumber = "01055110625";
		Profile myProfile = new Profile(myUser, "bio1", LocalDate.now(), "nickname1", PhoneNumber.of(phoneNumber));
		Profile friendProfile = new Profile(friendUser, "bio2", LocalDate.now(), "nickname2", PhoneNumber.of(phoneNumber));

		//when
		Friend friend = new Friend(myProfile, friendProfile);

		//then
		assertThat(friend.getMyProfile()).isEqualTo(myProfile);
		assertThat(friend.getFriendProfile()).isEqualTo(friendProfile);
		assertThat(friend.getStatus()).isEqualTo(Friend.Status.NORMAL);
	}

}
