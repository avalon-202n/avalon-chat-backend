package com.avalon.avalonchat.domain.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avalon.avalonchat.domain.profile.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	List<Profile> findAllByPhoneNumberIn(String[] phoneNumbers);

	Optional<Profile> findByPhoneNumber(String phoneNumbers);

	@Query("SELECT p.id "
		+ "FROM Profile p "
		+ "WHERE p.user.id = :userId")
	Optional<Long> findProfileIdByUserId(@Param("userId") long userId);
}
