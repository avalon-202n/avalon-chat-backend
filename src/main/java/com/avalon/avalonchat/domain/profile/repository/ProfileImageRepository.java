package com.avalon.avalonchat.domain.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}