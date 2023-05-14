package com.avalon.avalonchat.core.profile.application;

import java.util.List;

import com.avalon.avalonchat.core.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;

public interface ProfileService {
	ProfileAddResponse addProfile(long userId, ProfileAddRequest request);

	ProfileDetailedGetResponse getDetailedById(long profileId);

	List<ProfileListGetResponse> getListById(long profileId);
}
