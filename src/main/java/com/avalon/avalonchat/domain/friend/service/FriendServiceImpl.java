package com.avalon.avalonchat.domain.friend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

	private final FriendRepository friendRepository;
	private final ProfileRepository profileRepository;

	@Transactional
	@Override
	public FriendAddResponse addFriend(long userId, FriendAddRequest request) {
		// 1. find profile
		List<Profile> friendProfiles = profileRepository.findAllByPhoneNumber(request.getPhoneNumbers());
		Profile myProfile = profileRepository.findById(userId)
			.orElseThrow(() -> new AvalonChatRuntimeException("Profile Not Found"));

		// 2. create friends
		List<Friend> friends = new ArrayList<>();
		for (Profile friendProfile : friendProfiles) {
			Friend friend = new Friend(
				myProfile,
				friendProfile,
				Friend.FriendStatus.NORMAL
			);
			friends.add(friend);
		}

		// 3. save them
		// TODO: 벌크 연산에 transactionChunk 반영 -> 10,000건 단위인데, 전화번호 동기화를 한번에 이 이상 할 일이 있을까..?
		List<Friend> savedFriends = friendRepository.saveAll(friends);

		// 4. return
		return FriendAddResponse.ofEntity(savedFriends);
	}
}
