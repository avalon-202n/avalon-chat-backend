package com.avalon.avalonchat.domain.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.service.GetProfileIdService;
import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.PhoneNumberAuthenticationCode;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.PhoneNumberAuthenticationRepository;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileServiceImpl
	implements ProfileService, GetProfileIdService {

	private final ProfileRepository repository;
	private final UserRepository userRepository;
	private final PhoneNumberAuthenticationRepository phoneNumberAuthenticationRepository;

	@Transactional
	@Override
	public ProfileAddResponse addProfile(long userId, ProfileAddRequest request) {
		// 1. find user
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException("user", userId));

		// 2. check
		String phoneNumber = request.getPhoneNumber();
		PhoneNumberAuthenticationCode phoneNumberAuthenticationCode = phoneNumberAuthenticationRepository
			.findById(phoneNumber)
			.orElseThrow(() -> new NotFoundException("phoneNumber AuthenticationCode", phoneNumber));
		phoneNumberAuthenticationCode.checkAuthenticated();

		// 3. create profile
		Profile profile = new Profile(
			user,
			request.getBio(),
			request.getBirthDate(),
			request.getNickname(),
			phoneNumber
		);

		// 4. create images & add to profile
		profile.addProfileImage(new ProfileImage(profile, request.getProfileImageUrl()));
		profile.addBackgroundImage(new BackgroundImage(profile, request.getBackgroundImageUrl()));

		// 5. save it
		repository.save(profile);

		// 6. return
		return ProfileAddResponse.ofEntity(profile);
	}

	@Override
	public ProfileDetailedGetResponse getDetailedById(long profileId) {
		Profile profile = repository.findById(profileId)
			.orElseThrow(() -> new NotFoundException("profile", profileId));

		return ProfileDetailedGetResponse.from(profile);
	}

	@Override
	public long getProfileIdByUserId(long userId) {
		return repository.findProfileIdByUserId(userId)
			.orElseThrow(() -> new IllegalStateException("profile not found for userId :" + userId));
	}
}
