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

	@Transactional
	@Override
	public List<FriendAddResponse> addFriend(long profileId, FriendAddRequest request) {
		// 1. find profile & create friends
		Profile myProfile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		List<Friend> friends = profileRepository.findAllByPhoneNumberIn(request.getPhoneNumbers()).stream()
			.map(friendprofile -> new Friend(myProfile, friendprofile))
			.collect(Collectors.toList());

		// 2. save them & return
		return friendRepository.saveAll(friends).stream()
			.map(FriendAddResponse::from)
			.collect(Collectors.toList());
	}

	@Override
	public FriendStatusUpdateResponse updateFriendStatus(long myProfileId, long friendProfileId,
		FriendStatusUpdateRequest request) {
		// 1. find friend
		Friend friend = friendRepository.findByMyProfileIdAndFriendProfileId(myProfileId, friendProfileId)
			.orElseThrow(() -> new NotFoundException("friend", friendProfileId));

		// 2. update
		friend.updateStatus(request.getStatus());

		// 3. save & return
		friendRepository.save(friend);
		return FriendStatusUpdateResponse.ofEntity(friend);
	}
}
