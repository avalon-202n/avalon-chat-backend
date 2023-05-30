package com.avalon.avalonchat.core.friend.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
	boolean existsByFriendProfileId(long friendId);

	Optional<Friend> findByMyProfileIdAndFriendProfileId(long myProfileId, long friendProfileId);
}
