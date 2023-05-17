package com.avalon.avalonchat.core.profile.application;

import java.util.List;

import com.avalon.avalonchat.core.profile.dto.BackgroundImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateResponse;

public interface ProfileService {
	ProfileAddResponse addProfile(long userId, ProfileAddRequest request);

	ProfileDetailedGetResponse getDetailedById(long profileId);

	List<ProfileListGetResponse> getListById(long profileId);

	ProfileUpdateResponse updateProfile(long profileId, ProfileUpdateRequest request);

	void deleteProfileImage(long profileId, ProfileImageDeleteRequest request);

	void deleteBackgroundImage(long profileId, BackgroundImageDeleteRequest request);
}
