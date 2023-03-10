package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.dto.ProfileBioRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileBioResponse;

public interface ProfileService {
	ProfileBioResponse updateBio(Long id, ProfileBioRequest request);
}
