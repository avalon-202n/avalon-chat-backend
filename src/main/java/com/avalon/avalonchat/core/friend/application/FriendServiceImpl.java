package com.avalon.avalonchat.core.friend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.domain.FriendRepository;
import com.avalon.avalonchat.core.friend.dto.FriendEmailAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendEmailAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateResponse;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeRequest;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeResponse;
import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.user.domain.Email;
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

		Profile friendProfile = profileRepository.findByPhoneNumberAndNickname(request.getPhoneNumber(),
				request.getFriendProfileNickname())
			.orElseThrow(() -> new NotFoundException("friendProfile", request.getPhoneNumber()));

		// 2. check if already exists
		boolean exists
			= friendRepository.existsByFriendProfileIdAndMyProfileId(friendProfile.getId(), myProfile.getId());
		if (exists) {
			throw new BadRequestException("addFriendByPhoneNumber-failed.already-exists");
		}

		// 3. create friend & save them
		Friend friend = new Friend(myProfile, friendProfile, request.getFriendProfileNickname());
		Friend savedFriend = friendRepository.save(friend);

		// 4. create & return response
		return FriendPhoneNumberAddResponse.of(friendProfile, savedFriend);
	}

	@Override
	@Transactional
	public FriendEmailAddResponse addFriendByEmail(long profileId, FriendEmailAddRequest request) {
		// 1. find myProfile & friendProfile
		Profile myProfile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("myProfile", profileId));

		Profile friendProfile = profileRepository.findProfileByEmail(Email.of(request.getEmail()))
			.orElseThrow(() -> new NotFoundException("friendProfile", request.getEmail()));

		// 2. check if already exists
		boolean exists
			= friendRepository.existsByFriendProfileIdAndMyProfileId(friendProfile.getId(), myProfile.getId());
		if (exists) {
			throw new BadRequestException("addFriendByEmail-failed.already-exists");
		}

		// 3. create friend & save them
		Friend friend = new Friend(myProfile, friendProfile, friendProfile.getNickname());
		Friend savedFriend = friendRepository.save(friend);

		// 4. create $ return response
		return FriendEmailAddResponse.of(friendProfile, savedFriend);
	}

	@Override
	@Transactional
	public List<FriendSynchronizeResponse> synchronizeFriend(long profileId, FriendSynchronizeRequest request) {
		// 1. find myProfile
		Profile myProfile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		// 2. get phoneNumbers from request
		List<PhoneNumber> phoneNumbers = new ArrayList<>(request.getFriendsInfo().keySet());

		// 3. filter if already exists
		List<PhoneNumber> friendPhoneNumbers = profileRepository.findAllFriendPhoneNumbersByMyProfileId(
			myProfile.getId());

		List<Profile> friendProfiles = profileRepository.findAllByPhoneNumberIn(phoneNumbers).stream()
			.filter(profile -> !friendPhoneNumbers.contains(profile.getPhoneNumber()))
			.collect(Collectors.toList());

		// 4. create friends & responses
		List<Friend> friends = new ArrayList<>();
		List<FriendSynchronizeResponse> responses = new ArrayList<>();

		for (Profile friendProfile : friendProfiles) {
			Friend friend = new Friend(
				myProfile,
				friendProfile,
				request.getFriendsInfo().get(friendProfile.getPhoneNumber())
			);
			friends.add(friend);
			responses.add(FriendSynchronizeResponse.of(friendProfile, friend));
		}

		// 5. save them
		friendRepository.saveAll(friends);

		// 6. return responses
		return responses;
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
