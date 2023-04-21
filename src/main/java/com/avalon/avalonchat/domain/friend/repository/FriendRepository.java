package com.avalon.avalonchat.domain.friend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.domain.friend.domain.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {
	Optional<Friend> findByMyProfileIdAndFriendProfileId(long myProfileId, long friendProfileId);
}
