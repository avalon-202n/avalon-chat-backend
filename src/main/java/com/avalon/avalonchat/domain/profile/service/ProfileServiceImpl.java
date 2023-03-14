package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.domain.ImageUploader;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final ImageUploader imageUploader;

	@Override
	public ProfileAddResponse addProfile(long id, ProfileAddRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new AvalonChatRuntimeException("User Not Found"));

		Profile profile = profileRepository.save(request.toEntity(user));
		//TODO: 이미지 엔티티?? 질문..
		uploadImage(request);

		return ProfileAddResponse.ofEntity(profile);
	}

	private void uploadImage(ProfileAddRequest request) {
		request.getImage().uploadBy(imageUploader);
		request.getBackgroundImage().uploadBy(imageUploader);
	}
}
