package com.avalon.avalonchat.core.profile.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.core.user.domain.Email;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	List<Profile> findAllByPhoneNumberIn(List<String> phoneNumbers);

	Optional<Profile> findByPhoneNumber(String phoneNumber);

	Optional<Profile> findByPhoneNumberAndNickname(String phoneNumber, String nickname);

	@Query("SELECT p "
		+ "FROM  Profile p "
		+ "WHERE p.user.email = :email")
	Optional<Profile> findProfileByEmail(@Param("email") Email email);

	@Query("SELECT p.id "
		+ "FROM Profile p "
		+ "WHERE p.user.id = :userId")
	Optional<Long> findProfileIdByUserId(@Param("userId") long userId);

	@Query(
		"SELECT p "
			+ "FROM Profile p join fetch p.user u "
			+ "WHERE u.email = :email"
	)
	Optional<Profile> findByEmailWithUser(Email email);

	@Query(
		"SELECT new com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse("
			+ "p.nickname, "
			+ "p.bio, "
			+ "p.latestProfileImageUrl) "
			+ "FROM Profile p "
			+ "WHERE p.id IN (SELECT f.friendProfile.id FROM Friend f WHERE f.myProfile.id = :myProfileId) "
			+ "ORDER BY p.nickname")
	List<ProfileListGetResponse> findAllByMyProfileId(@Param("myProfileId") long myProfileId);

	@Query(
		"SELECT pi.url "
			+ "FROM Profile p "
			+ "INNER JOIN p.profileImages pi "
			+ "WHERE p.id = :profileId "
			+ "AND pi.createdAt = (SELECT MAX(i.createdAt) FROM ProfileImage i WHERE i.profile.id = :profileId)"
	)
	Optional<String> findLatestProfileImageUrl(@Param("profileId") long profileId);

	@Query("SELECT fp.phoneNumber "
		+ "FROM Friend f "
		+ "INNER JOIN f.friendProfile fp "
		+ "INNER JOIN f.myProfile mp "
		+ "WHERE mp.id = :myProfileId")
	List<String> findAllFriendPhoneNumbersByMyProfileId(@Param("myProfileId") long myProfileId);

	@Query(
		"SELECT p "
			+ "FROM Profile p join fetch p.user u "
			+ "WHERE u.id = :userId"
	)
	Optional<Profile> findByUserIdWithUser(long userId);
}
