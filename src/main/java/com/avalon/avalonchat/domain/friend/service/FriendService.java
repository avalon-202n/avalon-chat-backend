package com.avalon.avalonchat.domain.friend.service;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;

public interface FriendService {
	FriendAddResponse addFriend(long id, FriendAddRequest request);
}
