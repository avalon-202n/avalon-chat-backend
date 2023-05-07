package com.avalon.avalonchat.domain.friend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.profile.domain.Profile;

public interface FriendRepository extends JpaRepository<Friend, Long> {
	Optional<Friend> findByMyProfileIdAndFriendProfileId(long myProfileId, long friendProfileId);

	@Query("SELECT new com.avalon.avalonchat.domain.friend.dto.FriendAddResponse("
		+ "fp.id, "
		+ "fp.nickname, "
		+ "fp.bio, "
		+ "fp.latestProfileImageUrl, "
		+ "f.status) "
		+ "FROM Friend f "
		+ "INNER JOIN f.friendProfile fp "
		+ "WHERE f.id IN :friendIds")
	List<FriendAddResponse> findAllByFriendIds(@Param("friendIds") List<Long> friendIds);

	boolean existsByMyProfileAndFriendProfile(Profile myProfile, Profile friendProfile);
}
