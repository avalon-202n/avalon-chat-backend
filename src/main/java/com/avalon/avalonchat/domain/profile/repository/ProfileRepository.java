package com.avalon.avalonchat.domain.profile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avalon.avalonchat.domain.profile.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	@Query("select p from Profile p where p.phoneNumber in :phoneNumbers")
	List<Profile> findAllByPhoneNumber(@Param("phoneNumbers") String[] phoneNumbers);
}
