package com.avalon.avalonchat.domain.friend.service;

import java.util.List;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;

public interface FriendService {
	List<FriendAddResponse> addFriend(long id, FriendAddRequest request);
}
