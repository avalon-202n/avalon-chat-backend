package com.avalon.avalonchat.core.profile.application;

import java.util.List;

import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import com.avalon.avalonchat.core.profile.dto.BackgroundImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateResponse;
import com.avalon.avalonchat.core.user.domain.User;

public interface ProfileService {

	ProfileDetailedGetResponse getDetailedById(long profileId);

	List<ProfileListGetResponse> getListById(long profileId);

	ProfileUpdateResponse updateProfile(long profileId, ProfileUpdateRequest request);

	void deleteProfileImage(long profileId, ProfileImageDeleteRequest request);

	void deleteBackgroundImage(long profileId, BackgroundImageDeleteRequest request);

	ProfileDetailedGetResponse getFriendDetailedById(long myProfileId, long friendProfileId);

	void unitProfile(User user, PhoneNumber phoneNumber);
}
