package com.avalon.avalonchat.domain.profile.repository;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
