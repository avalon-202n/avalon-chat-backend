package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.image.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.image.ProfileImage;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;
import com.avalon.avalonchat.infra.image.upload.BackgroundImageRepository;
import com.avalon.avalonchat.infra.image.upload.ImageUploader;
import com.avalon.avalonchat.infra.image.upload.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final ImageUploader imageUploader;
	private static final String PROFILE_IMAGE = "profile";
	private static final String BACKGROUND_IMAGE = "background";
	private final ProfileImageRepository profileImageRepository;
	private final BackgroundImageRepository backgroundImageRepository;

	@Override
	public ProfileAddResponse addProfile(long id, ProfileAddRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new AvalonChatRuntimeException("User Not Found"));

		Profile profile = profileRepository.save(request.toEntity(user));

		Map<String, Object> map = uploadImage(profile, request);
		ProfileImage profileImage = (ProfileImage) map.get(PROFILE_IMAGE);
		BackgroundImage backgroundImage = (BackgroundImage) map.get(BACKGROUND_IMAGE);

		return ProfileAddResponse.ofEntity(profile, profileImage, backgroundImage);
	}

	private Map<String, Object> uploadImage(Profile profile, ProfileAddRequest request) {
		Map<String, Object> map = new HashMap<>();

		String profileImageUrl = request.getImage().uploadBy(imageUploader);
		String backgroundImageUrl = request.getBackgroundImage().uploadBy(imageUploader);

		ProfileImage profileImage = new ProfileImage(profile, profileImageUrl);
		BackgroundImage backgroundImage = new BackgroundImage(profile, backgroundImageUrl);
		ProfileImage savedProfileImage = profileImageRepository.save(profileImage);
		BackgroundImage savedBackgroundImage = backgroundImageRepository.save(backgroundImage);

		map.put(PROFILE_IMAGE, savedProfileImage);
		map.put(BACKGROUND_IMAGE, savedBackgroundImage);

		return map;
	}
}
