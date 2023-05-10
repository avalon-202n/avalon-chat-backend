package com.avalon.avalonchat.domain.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.service.GetProfileIdService;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileUpdateRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileUpdateResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.keyvalue.KeyAuthCodeValueStore;
import com.avalon.avalonchat.domain.user.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl
	implements ProfileService, GetProfileIdService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final KeyAuthCodeValueStore<PhoneNumberKey> phoneNumberKeyValueStore;

	@Transactional
	@Override
	public ProfileAddResponse addProfile(long userId, ProfileAddRequest request) {
		// 1. find user
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException("user", userId));

		// 2. check
		String phoneNumber = request.getPhoneNumber();
		if (!phoneNumberKeyValueStore.isAuthenticated(PhoneNumberKey.fromString(phoneNumber))) {
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

		// 3. update images
		if (request.isProfileImageAdded()) {
			profile.addProfileImage(request.getProfileImageUrl());
		}
		if (request.isBackgroundImageAdded()) {
			profile.addBackgroundImage(request.getBackgroundImageUrl());
		}

		request.getDeletedProfileImageIndexes().stream()
			.forEach(index -> profile.deleteProfileImage(index));
		request.getDeletedBackgroundImageIndexes().stream()
			.forEach(index -> profile.deleteBackgroundImage(index));

		// 4. return
		return ProfileUpdateResponse.from(profile);
	}

	@Override
	public long getProfileIdByUserId(long userId) {
		return profileRepository.findProfileIdByUserId(userId)
			.orElseThrow(() -> new IllegalStateException("profile not found for userId :" + userId));
	}
}
