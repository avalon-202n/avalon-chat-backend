package com.avalon.avalonchat.domain.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;

public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Long> {
	Optional<BackgroundImage> findByProfile(Profile profile);
}
