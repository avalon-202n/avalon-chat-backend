package com.avalon.avalonchat.domain.friend.service;

import java.util.List;
import java.util.stream.Collectors;

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
	public List<FriendAddResponse> addFriend(long profileId, FriendAddRequest request) {
		// 1. find profile & create friends
		Profile myProfile = profileRepository.findById(profileId)
			.orElseThrow(() -> new AvalonChatRuntimeException("Profile Not Found"));

		List<Friend> friends = profileRepository.findAllByPhoneNumberIn(request.getPhoneNumbers()).stream()
			.map(friendprofile -> new Friend(myProfile, friendprofile))
			.collect(Collectors.toList());

		// 2. save them & return
		return friendRepository.saveAll(friends).stream()
			.map(savedFriend -> FriendAddResponse.ofEntity(savedFriend))
			.collect(Collectors.toList());
	}
}
