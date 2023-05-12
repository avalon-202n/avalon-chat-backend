package com.avalon.avalonchat.core.profile.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.login.application.GetProfileIdService;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.core.user.keyvalue.KeyAuthCodeValueStore;
import com.avalon.avalonchat.core.user.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl
	implements ProfileService, GetProfileIdService {

	private final ProfileRepository repository;
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
		repository.save(profile);

		// 6. return
		return ProfileAddResponse.from(profile);
	}

	@Override
	public ProfileDetailedGetResponse getDetailedById(long profileId) {
		Profile profile = repository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		return ProfileDetailedGetResponse.from(profile);
	}

	@Override
	public List<ProfileListGetResponse> getListById(long profileId) {
		// find friendProfiles & return
		return repository.findAllByMyProfileId(profileId);
	}

	@Override
	public long getProfileIdByUserId(long userId) {
		return repository.findProfileIdByUserId(userId)
			.orElseThrow(() -> new IllegalStateException("profile not found for userId :" + userId));
	}
}
