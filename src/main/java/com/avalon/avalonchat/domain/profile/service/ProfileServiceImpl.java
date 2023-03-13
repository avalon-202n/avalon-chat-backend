package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.domain.ImageUploader;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.dto.ProfileRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileImageRespository;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final ProfileImageRespository profileImageRespository;
	private final UserRepository userRepository;
	private final ImageUploader imageUploader;

	@Override
	public ResponseEntity<ProfileResponse> addProfile(long id, ProfileRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new AvalonChatRuntimeException("User Not Found"));

		Profile profile = profileRepository.save(request.toEntity(user));
		HashMap<String, ProfileImage> images = uploadImage(request, profile);

		ProfileResponse profileResponse = ProfileResponse.ofEntity(profile, images.get("image"), images.get("backgroundImage"));
		return new ResponseEntity<>(profileResponse, HttpStatus.CREATED);
	}

	private HashMap<String, ProfileImage> uploadImage(ProfileRequest request, Profile profile) {
		HashMap<String, ProfileImage> images = new HashMap<>();

		ProfileImage profileImage = request.getImage().toEntity(profile, imageUploader);
		images.put("image", profileImageRespository.save(profileImage));
		ProfileImage profileBackgroundImage = request.getBackgroundImage().toEntity(profile, imageUploader);
		images.put("backgroundImage", profileImageRespository.save(profileBackgroundImage));

		return images;
	}
}
