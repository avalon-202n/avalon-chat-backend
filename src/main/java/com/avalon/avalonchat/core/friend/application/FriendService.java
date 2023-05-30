package com.avalon.avalonchat.core.friend.application;

import java.util.List;

import com.avalon.avalonchat.core.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateResponse;

public interface FriendService {

	FriendPhoneNumberAddResponse addFriendByPhoneNumber(long profileId, FriendPhoneNumberAddRequest request);

	/**
	 * add friends by given request(phonenumber list)
	 *
	 * @param profileId - current user profileId
	 * @param request   - phonenumber list
	 * @return - added Friend profiles
	 */
	List<FriendAddResponse> addFriend(long profileId, FriendAddRequest request);

	FriendStatusUpdateResponse updateFriendStatus(
		long myProfileId,
		long friendProfileId,
		FriendStatusUpdateRequest request
	);
}
