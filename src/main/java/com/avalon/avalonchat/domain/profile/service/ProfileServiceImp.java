package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.ProfileBioRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileBioResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImp implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;

	@Override
	public ProfileBioResponse updateBio(Long id, ProfileBioRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("User Not Found"));
		Optional<Profile> optionalProfile = profileRepository.findByUser(user);
		if (optionalProfile.isPresent()) {
			Profile profile = optionalProfile.get();
			profile.toEditBio(user, request);
			profileRepository.save(profile);
			return ProfileBioResponse.ofEntity(profile);
		}
		Profile profile = profileRepository.save(request.toEntity(user));
		return ProfileBioResponse.ofEntity(profile);
	}
}
