package com.avalon.avalonchat.domain.profile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.domain.profile.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	List<Profile> findAllByPhoneNumberIn(String[] phoneNumbers);
}
