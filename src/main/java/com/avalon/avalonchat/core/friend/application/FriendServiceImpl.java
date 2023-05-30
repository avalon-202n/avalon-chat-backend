package com.avalon.avalonchat.core.friend.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.domain.FriendRepository;
import com.avalon.avalonchat.core.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateResponse;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
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
	public FriendPhoneNumberAddResponse addFriendByPhoneNumber(long profileId, FriendPhoneNumberAddRequest request) {
		// 1. find myProfile & friendProfile
		Profile myProfile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("myProfile", profileId));

		Profile friendProfile = profileRepository.findByPhoneNumber(request.getPhoneNumber())
			.orElseThrow(() -> new NotFoundException("friendProfile", request.getPhoneNumber()));

		// 2. check if already exists
		boolean exists = friendRepository.existsByFriendProfileId(friendProfile.getId());
		if (exists) {
			throw new BadRequestException("addFriendByPhoneNumber-failed.already-exists");
		}

		// 3. create friend & save them
		Friend friend = new Friend(myProfile, friendProfile, request.getDisplayName());
		Friend savedFriend = friendRepository.save(friend);

		// 4. create & return response
		return FriendPhoneNumberAddResponse.of(friendProfile, savedFriend);
	}

	@Override
	@Transactional
	public List<FriendAddResponse> addFriend(long profileId, FriendAddRequest request) {
		// 1. find profile & create friends
		Profile myProfile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		List<String> friendPhoneNumbers = profileRepository.findAllFriendPhoneNumbersByMyProfileId(myProfile.getId());

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
