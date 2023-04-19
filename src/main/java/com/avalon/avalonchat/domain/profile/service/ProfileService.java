package com.avalon.avalonchat.domain.profile.service;

import java.util.List;

import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileListGetResponse;

public interface ProfileService {
	ProfileAddResponse addProfile(long userId, ProfileAddRequest request);

	ProfileDetailedGetResponse getDetailedById(long profileId);

	List<ProfileListGetResponse> getListById(long profileId);
}
