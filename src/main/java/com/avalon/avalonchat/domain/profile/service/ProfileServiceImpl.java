package com.avalon.avalonchat.domain.profile.service;

import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final ProfileImageRepository profileImageRepository;
	private final BackgroundImageRepository backgroundImageRepository;

	@Override
	public ProfileAddResponse addProfile(long id, ProfileAddRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new AvalonChatRuntimeException("User Not Found"));

		Profile profile = profileRepository.save(request.toEntity(user));

		ProfileImage profileImage = profileImageRepository.save(request.getProfileImage().toEntity(profile));
		BackgroundImage backgroundImage = backgroundImageRepository.save(
			request.getBackgroundImage().toEntity(profile));

		return ProfileAddResponse.ofEntity(profile, profileImage, backgroundImage);
	}
}
