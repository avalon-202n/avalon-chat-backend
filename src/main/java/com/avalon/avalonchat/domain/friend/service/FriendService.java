package com.avalon.avalonchat.domain.friend.service;

import java.util.List;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateResponse;

public interface FriendService {
	List<FriendAddResponse> addFriend(long id, FriendAddRequest request);

	FriendStatusUpdateResponse updateFriendStatus(long myProfileId, long friendProfileId,
		FriendStatusUpdateRequest request);
}
