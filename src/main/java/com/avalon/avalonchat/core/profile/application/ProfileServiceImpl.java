package com.avalon.avalonchat.core.profile.application;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.domain.FriendRepository;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.profile.dto.BackgroundImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateResponse;
import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final FriendRepository friendRepository;
	private final PhoneNumberAuthCodeStore phoneNumberKeyValueStore;

	@Transactional
	@Override
	public ProfileAddResponse addProfile(long userId, ProfileAddRequest request) {
		// 1. find user
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException("user", userId));

		// 2. check
		String phoneNumber = request.getPhoneNumber();
		boolean authenticated = !phoneNumberKeyValueStore.isAuthenticated(PhoneNumberKey.fromString(phoneNumber));
		if (authenticated) {
			throw new BadRequestException("phonenumber.no-auth", phoneNumber);
		}

		// 3. create profile
		Profile profile = new Profile(
			user,
			request.getBio(),
			request.getBirthDate(),
			request.getNickname(),
			phoneNumber
		);

		// 4. create images & add to profile
		profile.addProfileImage(request.getProfileImageUrl());
		profile.addBackgroundImage(request.getBackgroundImageUrl());

		// 5. save it
		profileRepository.save(profile);

		// 6. return
		return ProfileAddResponse.from(profile);
	}

	@Override
	public ProfileDetailedGetResponse getDetailedById(long profileId) {
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		return ProfileDetailedGetResponse.from(profile);
	}

	@Override
	public List<ProfileListGetResponse> getListById(long profileId) {
		// find friendProfiles & return
		return profileRepository.findAllByMyProfileId(profileId);
	}

	@Transactional
	@Override
	public ProfileUpdateResponse updateProfile(long profileId, ProfileUpdateRequest request) {
		// 1. find profile
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		// 2. update profile
		profile.update(
			request.getBio(), request.getBirthDate(), request.getNickname(), request.getPhoneNumber()
		);

		// 3. update images if not null or empty
		if (!StringUtils.isEmpty(request.getProfileImageUrl())) {
			profile.addProfileImage(request.getProfileImageUrl());
		}
		if (!StringUtils.isEmpty(request.getBackgroundImageUrl())) {
			profile.addBackgroundImage(request.getBackgroundImageUrl());
		}

		// 4. return
		return ProfileUpdateResponse.from(profile);
	}

	@Transactional
	@Override
	public void deleteProfileImage(long profileId, ProfileImageDeleteRequest request) {
		// 1. find profile
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		// 2. delete profileImages
		profile.deleteProfileImage(request.getDeletedProfileImageUrl());

		// 3. update latestProfileImageUrl
		Optional<String> optionalLatestProfileImageUrl = profileRepository.findLatestProfileImageUrl(profileId);
		if (optionalLatestProfileImageUrl.isPresent()) {
			profile.updateLatestProfileImageUrl(optionalLatestProfileImageUrl.get());
		} else {
			profile.updateLatestProfileImageUrl(null);
		}
	}

	@Transactional
	@Override
	public void deleteBackgroundImage(long profileId, BackgroundImageDeleteRequest request) {
		// 1. find profile
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		// 2. delete backgroundImages
		profile.deleteBackgroundImage(request.getDeletedBackgroundImageUrl());
	}

	@Override
	public ProfileDetailedGetResponse getFriendDetailedById(long myProfileId, long friendProfileId) {
		// 1. 내 친구 프로필 아이디인지 확인
		Friend friend = friendRepository.findByMyProfileIdAndFriendProfileId(myProfileId, friendProfileId)
			.orElseThrow(() -> new NotFoundException("friend", friendProfileId));
		// 2. 프로필 조회
		return getDetailedById(friendProfileId);
	}
}
