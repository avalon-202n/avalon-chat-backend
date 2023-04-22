package com.avalon.avalonchat.domain.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	List<Profile> findAllByPhoneNumberIn(List<String> phoneNumbers);

	Optional<Email> findByPhoneNumber(String phoneNumbers);

	@Query("SELECT p.id "
		+ "FROM Profile p "
		+ "WHERE p.user.id = :userId")
	Optional<Long> findProfileIdByUserId(@Param("userId") long userId);

	Optional<Profile> findByUser(User user);

	@Query("SELECT new com.avalon.avalonchat.domain.profile.dto.ProfileListGetResponse(p.nickname, p.bio, pi.url) "
		+ "FROM Profile p "
		+ "INNER JOIN p.profileImages pi "
		+ "WHERE p.id IN (SELECT f.friendProfile.id FROM Friend f WHERE f.myProfile.id = :myProfileId) "
		+ "AND pi.createdAt = (SELECT MAX(i.createdAt) FROM ProfileImage i WHERE i.profile.id = p.id) "
		+ "ORDER BY p.nickname")
	List<ProfileListGetResponse> findAllByMyProfileId(@Param("myProfileId") long myProfileId);
}
