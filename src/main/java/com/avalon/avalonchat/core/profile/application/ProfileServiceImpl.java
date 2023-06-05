package com.avalon.avalonchat.core.profile.application;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.domain.FriendRepository;
import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
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
	public ProfileAddResponse addProfile(long profileId, ProfileAddRequest request) {
		// 1. find user
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));
		User findUser = profile.getUser();

		// 2. check profile 생성 여부
		if (findUser.getIsCreateProfileStatus()) {
			throw new BadRequestException("profile-add-failed.already-create");
		}

		// 3. update profile
		profile.update(
			request.getBio(), request.getBirthDate(), request.getNickname()
		);

		// 4. create images & add to profile
		profile.addProfileImage(request.getProfileImageUrl());
		profile.addBackgroundImage(request.getBackgroundImageUrl());

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
			request.getBio(), request.getBirthDate(), request.getNickname()
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

		// 2. validation
		if (request.getDeletedProfileImageUrls().size() == 0) {
			throw new BadRequestException("profileImageDelete-failed.empty-parameter");
		}
		if (request.getDeletedProfileImageUrls().size() > profile.getProfileImages().size()) {
			throw new BadRequestException("profileImageDelete-failed.index-out-of-range");
		}

		// 3. delete profileImages
		profile.deleteProfileImage(request.getDeletedProfileImageUrls());

		// 4. update latestProfileImageUrl
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

		// 2. validation
		if (request.getDeletedBackgroundImageUrls().size() == 0) {
			throw new BadRequestException("backgroundImageDelete-failed.empty-parameter");
		}
		if (request.getDeletedBackgroundImageUrls().size() > profile.getBackgroundImages().size()) {
			throw new BadRequestException("backgroundImageDelete-failed.index-out-of-range");
		}

		// 3. delete backgroundImages
		profile.deleteBackgroundImage(request.getDeletedBackgroundImageUrls());
	}

	@Override
	public ProfileDetailedGetResponse getFriendDetailedById(long myProfileId, long friendProfileId) {
		// 1. 내 친구 프로필 아이디인지 확인
		Friend friend = friendRepository.findByMyProfileIdAndFriendProfileId(myProfileId, friendProfileId)
			.orElseThrow(() -> new NotFoundException("friend", friendProfileId));
		// 2. 프로필 조회
		return getDetailedById(friendProfileId);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	@Override
	public void unitProfile(User user, PhoneNumber phoneNumber) {
		Profile profile = new Profile(user, phoneNumber);
		profileRepository.save(profile);
	}
}
