package com.avalon.avalonchat.domain.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.repository.BackgroundImageRepository;
import com.avalon.avalonchat.domain.profile.repository.ProfileImageRepository;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final ProfileImageRepository profileImageRepository;
	private final BackgroundImageRepository backgroundImageRepository;

	@Transactional
	@Override
	public ProfileAddResponse addProfile(long userId, ProfileAddRequest request) {
		// 1. find user
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AvalonChatRuntimeException("User Not Found"));

		// 2. create profile
		Profile profile = new Profile(
			user,
			request.getBio(),
			request.getBirthDate(),
			request.getNickname()
		);

		// 3. save it
		Profile savedProfile = profileRepository.save(profile);

		// 4. create images
		ProfileImage profileImage = new ProfileImage(savedProfile, request.getProfileImage());
		BackgroundImage backgroundImage = new BackgroundImage(savedProfile, request.getBackgroundImage());

		// 5. save them - TODO jpa cascade persists?
		profileImageRepository.save(profileImage);
		backgroundImageRepository.save(backgroundImage);

		// 6. return
		return ProfileAddResponse.ofEntity(profile, profileImage, backgroundImage);
	}
}
