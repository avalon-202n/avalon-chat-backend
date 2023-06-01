package com.avalon.avalonchat.core.friend.application;

import java.util.List;

import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateResponse;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeRequest;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeResponse;

public interface FriendService {

	FriendPhoneNumberAddResponse addFriendByPhoneNumber(long profileId, FriendPhoneNumberAddRequest request);

	/**
	 * add friends by given request(phonenumber list)
	 *
	 * @param profileId - current user profileId
	 * @param request   - phonenumber list
	 * @return - added Friend profiles
	 */
	List<FriendSynchronizeResponse> synchronizeFriend(long profileId, FriendSynchronizeRequest request);

	FriendStatusUpdateResponse updateFriendStatus(
		long myProfileId,
		long friendProfileId,
		FriendStatusUpdateRequest request
	);
}
