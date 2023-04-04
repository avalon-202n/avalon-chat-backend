package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileFindRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileFindResponse;

public interface ProfileService {

	ProfileAddResponse addProfile(long userId, ProfileAddRequest request);

	ProfileFindResponse findProfile(ProfileFindRequest request);
}
