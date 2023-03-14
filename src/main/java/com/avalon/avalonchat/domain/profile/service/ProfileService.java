package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;

public interface ProfileService {
	ProfileAddResponse addProfile(long id, ProfileAddRequest request);
}
