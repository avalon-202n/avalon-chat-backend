package com.avalon.avalonchat.domain.friend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateResponse;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FriendServiceImpl implements FriendService {

	private final FriendRepository friendRepository;
	private final ProfileRepository profileRepository;

	@Override
	@Transactional
	public List<FriendAddResponse> addFriend(long profileId, FriendAddRequest request) {
		// 1. find profile & create friends
		Profile myProfile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		List<String> friendPhoneNumbers = friendRepository.findAllFriendPhoneNumbersByMyProfileId(myProfile.getId());

		List<Friend> friends = profileRepository.findAllByPhoneNumberIn(request.getPhoneNumbers()).stream()
			.filter(profile -> !friendPhoneNumbers.contains(profile.getPhoneNumber()))
			.map(profile -> new Friend(myProfile, profile))
			.collect(Collectors.toList());

		// 2. save them
		List<Long> friendIds = friendRepository.saveAll(friends).stream()
			.map(Friend::getId)
			.collect(Collectors.toList());

		// 3. return
		return friendRepository.findAllByFriendIds(friendIds);
	}

	@Override
	@Transactional
	public FriendStatusUpdateResponse updateFriendStatus(long myProfileId, long friendProfileId,
		FriendStatusUpdateRequest request) {
		// 1. find friend
		Friend friend = friendRepository.findByMyProfileIdAndFriendProfileId(myProfileId, friendProfileId)
			.orElseThrow(() -> new NotFoundException("friend", friendProfileId));

		// 2. update
		friend.updateStatus(request.getStatus());

		// 3. save & return
		return FriendStatusUpdateResponse.ofEntity(friend);
	}
}
