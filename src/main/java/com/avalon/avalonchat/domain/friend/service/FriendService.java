package com.avalon.avalonchat.domain.friend.service;

import java.util.List;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateResponse;

public interface FriendService {

	/**
	 * add friends by given request(phonenumber list)
	 *
	 * @param profileId - current user profileId
	 * @param request   - phonenumber list
	 * @return - added Friend profiles
	 */
	// TODO - do not response List as Response
	List<FriendAddResponse> addFriend(long profileId, FriendAddRequest request);

	FriendStatusUpdateResponse updateFriendStatus(
		long myProfileId,
		long friendProfileId,
		FriendStatusUpdateRequest request
	);
}
