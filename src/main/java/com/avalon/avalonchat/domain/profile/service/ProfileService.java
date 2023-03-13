package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.dto.ProfileRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileResponse;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
	ResponseEntity<ProfileResponse> addProfile(long id, ProfileRequest request);
}
