package com.avalon.avalonchat.domain.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.service.GetProfileIdService;
import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.PhoneNumberAuthenticationCode;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.PhoneNumberAuthenticationRepository;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileServiceImpl
	implements ProfileService, GetProfileIdService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final PhoneNumberAuthenticationRepository phoneNumberAuthenticationRepository;

	@Transactional
	@Override
	public ProfileAddResponse addProfile(long userId, ProfileAddRequest request) {
		// 1. find user
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AvalonChatRuntimeException("User Not Found"));

		// 2. check
		String phoneNumber = request.getPhoneNumber();
		PhoneNumberAuthenticationCode phoneNumberAuthenticationCode = phoneNumberAuthenticationRepository.findById(
			phoneNumber).orElseThrow(() -> new AvalonChatRuntimeException("인증번호를 받지 않은 사용자입니다."));
		if (!phoneNumberAuthenticationCode.isAuthenticated()) {
			throw new AvalonChatRuntimeException("인증되지 않은 휴대전화번호 입니다.");
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
		saveImages(profile, request);

		// 5. save it
		profileRepository.save(profile);

		// 6. return
		return ProfileAddResponse.ofEntity(profile);
	}

	private void saveImages(Profile profile, ProfileAddRequest request) {
		// 1. create images
		ProfileImage profileImage = new ProfileImage(profile, request.getProfileImageUrl());
		BackgroundImage backgroundImage = new BackgroundImage(profile, request.getBackgroundImageUrl());

		// 2. add to profile
		profile.addProfileImage(profileImage);
		profile.addBackgroundImage(backgroundImage);
	}

	@Override
	public long getProfileIdByUserId(long userId) {
		return profileRepository.findProfileIdByUserId(userId)
			.orElseThrow(() -> new IllegalStateException("profile not found for userId :" + userId));
	}
}
