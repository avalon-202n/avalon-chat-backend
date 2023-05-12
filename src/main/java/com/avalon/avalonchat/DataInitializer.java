package com.avalon.avalonchat;

import static java.time.LocalDate.*;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.avalon.avalonchat.core.chat.domain.ChatMessageRepository;
import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.repository.FriendRepository;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.repository.ProfileRepository;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final FriendRepository friendRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * initializing scenario
	 * - 사용자 총 3명 등록
	 * - 친구 관계 : "user1" <-> "user2" -> "user3" (user3 은 자신의 친구가 없음)
	 * - 채팅 기록: // 아직 못함 설계 부족
	 * "user1" -> "user2" : "안녕 ~"
	 * "user2" -> "user3" : "너도 안녕 ~"
	 * "user2" -> "user3" : "안녕하세요"
	 * "user3" -> "user2" : "누구세요?"
	 */
	@Override
	public void run(ApplicationArguments args) {
		// 잘못된 사용방식이지만 사용함 FIXME!
		Password password1 = Password.of("fixme fixme");
		password1.setEncryptedPassword(passwordEncoder.encode("password1"));
		User user1 = userRepository.save(new User(Email.of("user111@gmail.com"), password1));

		Password password2 = Password.of("fixme fixme");
		password2.setEncryptedPassword(passwordEncoder.encode("password2"));
		User user2 = userRepository.save(new User(Email.of("user222@gmail.com"), password2));

		Password password3 = Password.of("fixme fixme");
		password3.setEncryptedPassword(passwordEncoder.encode("password3"));
		User user3 = userRepository.save(new User(Email.of("user333@gmail.com"), password3));

		Profile profile1 = profileRepository.save(new Profile(user1, "bio1", now(), "user1", "010-1111-1111"));
		Profile profile2 = profileRepository.save(new Profile(user2, "bio2", now(), "user2", "010-2222-2222"));
		Profile profile3 = profileRepository.save(new Profile(user3, "bio3", now(), "user3", "010-3333-3333"));

		Friend friend12 = friendRepository.save(new Friend(profile1, profile2));
		Friend friend21 = friendRepository.save(new Friend(profile2, profile1));
		Friend friend23 = friendRepository.save(new Friend(profile2, profile3));
	}
}
